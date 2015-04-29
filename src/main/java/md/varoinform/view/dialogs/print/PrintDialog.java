package md.varoinform.view.dialogs.print;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.view.LanguageComboBox;
import md.varoinform.view.demonstrator.Demonstrator;
import md.varoinform.view.dialogs.FieldChoosePanel;
import md.varoinform.view.dialogs.RowsChoosePanel;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/26/13
 * Time: 11:24 AM
 */
public class PrintDialog extends JDialog {
    private static final int DATA_MODE = 1;
    private static final int ADDRESS_MODE = 2;
    private final RowsChoosePanel rowsChooser;


    private int mode = DATA_MODE;
    private final FieldChoosePanel fieldChoosePanel;
    private final HashPrintRequestAttributeSet attributes;
    private PageFormat pageFormat;
    private Demonstrator demonstrator;
    private final LanguageComboBox languageCombo;


    public PrintDialog( Demonstrator demonstrator) {
        this.demonstrator = demonstrator;

        setSize(700, 600);
        setModal(true);
        updateTitle();
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        JRadioButton typeAddress = new JRadioButton(ResourceBundleHelper.getString("type-address", "Address"));
        typeAddress.addActionListener(new PrintModeAction(ADDRESS_MODE));
        JRadioButton typeData = new JRadioButton(ResourceBundleHelper.getString("type-data", "Data"));
        typeData.addActionListener(new PrintModeAction(DATA_MODE));
        typeData.setSelected(true);
        JPanel typePanel = createRadioButtonGroup(null, new JRadioButton[]{typeData, typeAddress});
        panel.add(typePanel);


        rowsChooser = new RowsChoosePanel("print", demonstrator.getSelected().size(), demonstrator.getALL().size());
        panel.add(rowsChooser);


        JPanel languagePanel = new JPanel();
        languagePanel.setBorder(getTitledBorder(ResourceBundleHelper.getString("language_choose_panel_title", "Choose language")));

        languageCombo = new LanguageComboBox();
        languagePanel.add(languageCombo);
        panel.add(languagePanel);

        attributes = new HashPrintRequestAttributeSet();
        JPanel buttonPanel = new JPanel();
        JButton printButton = new JButton(ResourceBundleHelper.getString("print", "Print"));
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, ResourceBundleHelper.getString("printing_warning", "Are you sure?"));
                if (result == JOptionPane.YES_OPTION){
                    PrinterJob job = PrinterJob.getPrinterJob();
                    Book book = makeBook();
                    job.setPageable(book);
                    if (job.printDialog(attributes)) {
                        PrintActivity printActivity = new PrintActivity(job, attributes);
                        printActivity.execute();
                    }
                }
                setVisible(false);
            }
        });
        buttonPanel.add(printButton);


        JButton printPreviewButton = new JButton(ResourceBundleHelper.getString("print-preview", "Preview"));
        buttonPanel.add(printPreviewButton);
        printPreviewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PrintPreviewDialog dialog = new PrintPreviewDialog(makeBook());
                dialog.setVisible(true);
            }
        });

        JButton pageSetupButton = new JButton(ResourceBundleHelper.getString("print-settings", "Settings"));
        buttonPanel.add(pageSetupButton);
        pageSetupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PrinterJob job = PrinterJob.getPrinterJob();
                pageFormat = job.pageDialog(attributes);
            }
        });



        add(panel, BorderLayout.CENTER);
        fieldChoosePanel = new FieldChoosePanel();
        JScrollPane choosePane = new JScrollPane(fieldChoosePanel);
        choosePane.setBorder(getTitledBorder(ResourceBundleHelper.getString("fields", "Fields")));
        add(choosePane, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);

    }

    private void updateTitle() {
        setTitle(ResourceBundleHelper.getString("print", "Print"));
    }

    private Book makeBook() {
        if (pageFormat == null){
            PrinterJob job = PrinterJob.getPrinterJob();
            pageFormat = job.defaultPage();
        }

        Book book = new Book();
        Printable painter = null;
        int numPages = 0;
        if (mode == DATA_MODE){
            painter = new Data(pageFormat, getEnterprises(), getSelectedFields(), (Integer) languageCombo.getSelectedItem());
            numPages = ((Data)painter).getNumPages();

        } else if (mode == ADDRESS_MODE){
            painter = new Address(getEnterprises(), (Integer) languageCombo.getSelectedItem());
            numPages = ((Address)painter).getNumPages(pageFormat);
        }
        if (painter != null)
            book.append(painter, pageFormat, numPages);
        return book;
    }

    private List<Integer> getEnterprises() {
        List<Integer> enterpriseIds = new ArrayList<>();
        if (rowsChooser.getChoose() == RowsChoosePanel.ALL) {
            enterpriseIds = demonstrator.getALL();

        } else if (rowsChooser.getChoose() == RowsChoosePanel.SELECTED) {
            enterpriseIds = demonstrator.getSelected();
        }

        return enterpriseIds;
    }

    private JPanel createRadioButtonGroup(String title, JRadioButton[] buttons) {
        JPanel typePanel = new JPanel();
        typePanel.setLayout(new BoxLayout(typePanel, BoxLayout.Y_AXIS));
        if (title != null && !title.isEmpty()) {
            typePanel.setBorder(getTitledBorder(ResourceBundleHelper.getString(title, title)));
        }

        ButtonGroup buttonGroup = new ButtonGroup();
        for (JRadioButton button : buttons) {
            buttonGroup.add(button);
            typePanel.add(button);
        }

        return typePanel;
    }

    private TitledBorder getTitledBorder(String title) {
        return BorderFactory.createTitledBorder(title);
    }

    private List<String> getSelectedFields() {
        return fieldChoosePanel.getSelectedFieldNames();
    }

    private class PrintModeAction extends AbstractAction {
        private final int value;
        private final String ru = "ru";

        public PrintModeAction(int value) {
            this.value = value;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            mode = this.value;
            Integer lang = LanguageProxy.instance.getLanguage(ru);
            if (mode == ADDRESS_MODE){
                fieldChoosePanel.setEnabled(false);
                languageCombo.setEnableItem(lang, false);
            } else {
                fieldChoosePanel.setEnabled(true);
                languageCombo.setEnableItem(lang, true);
            }
        }

    }

    public static void print(Demonstrator demonstrator) {
        PrintDialog printDialog = new PrintDialog(demonstrator);
        printDialog.setVisible(true);
        printDialog.dispose();
    }
}
