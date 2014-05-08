package md.varoinform.view.dialogs;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.entities.Language;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.view.demonstrator.Demonstrator;
import md.varoinform.view.dialogs.preview.Address;
import md.varoinform.view.dialogs.preview.Data;
import md.varoinform.view.dialogs.preview.PrintPreviewDialog;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.print.*;
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
    private Language language = LanguageProxy.instance.getCurrentLanguage();
    private final FieldChoosePanel fieldChoosePanel = new FieldChoosePanel();
    private final HashPrintRequestAttributeSet attributes;
    private PageFormat pageFormat;
    private Demonstrator demonstrator;
    private final JComboBox<Object> languageCombo;


    public PrintDialog( Demonstrator demonstrator) {
        this.demonstrator = demonstrator;

        setSize(450, 500);
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
        JPanel typePanel = createRadioButtonGroup("print value", new JRadioButton[]{typeData, typeAddress});
        panel.add(typePanel);


        rowsChooser = new RowsChoosePanel("print", demonstrator.getSelected().size(), demonstrator.getALL().size());
        panel.add(rowsChooser);


        JPanel languagePanel = new JPanel();
        languagePanel.setBorder(getTitledBorder(ResourceBundleHelper.getString("language-choose", "Choose language")));
        List<Language> languages = LanguageProxy.instance.getLanguages();
        languageCombo = new JComboBox<>(languages.toArray());
        languageCombo.setSelectedIndex(languages.indexOf(language));
        languageCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    language = (Language) e.getItem();
                }
            }
        });

        languagePanel.add(languageCombo);
        panel.add(languagePanel);

        attributes = new HashPrintRequestAttributeSet();
        JPanel buttonPanel = new JPanel();
        JButton printButton = new JButton(ResourceBundleHelper.getString("print", "Print"));
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PrinterJob job = PrinterJob.getPrinterJob();
                    job.setPageable(makeBook());
                    if (job.printDialog(attributes)) {
                        job.print(attributes);
                    }
                } catch (PrinterException e1) {
                    e1.printStackTrace();
                }
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
        List<Enterprise> enterprises = getEnterprises();

        if (mode == DATA_MODE){
            Data data = new Data(enterprises, getSelectedFields(), language);
            int pageCount = data.getPageCount((Graphics2D)getGraphics(), pageFormat);
            book.append(data, pageFormat, pageCount);

        } else if (mode == ADDRESS_MODE){
            Address address = new Address(enterprises, language);
            int pageCount = address.getPageCount(pageFormat);
            book.append(address, pageFormat, pageCount);
        }

        return book;
    }

    private List<Enterprise> getEnterprises() {
        List<Enterprise> enterprises = null;
        if (rowsChooser.getChoose() == RowsChoosePanel.ALL) {
            enterprises = demonstrator.getALL();

        } else if (rowsChooser.getChoose() == RowsChoosePanel.SELECTED) {
            enterprises = demonstrator.getSelected();
        }

        return enterprises;
    }

    private JPanel createRadioButtonGroup(String title, JRadioButton[] buttons) {
        JPanel typePanel = new JPanel();
        typePanel.setLayout(new BoxLayout(typePanel, BoxLayout.Y_AXIS));
        typePanel.setBorder(getTitledBorder(ResourceBundleHelper.getString(title, title)));

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

    public void updateDisplay() {
        fieldChoosePanel.updateDisplay();
        language = LanguageProxy.instance.getCurrentLanguage();
        languageCombo.setSelectedItem(language);
        updateTitle();

    }


    private class PrintModeAction extends AbstractAction {
        private final int value;

        public PrintModeAction(int value) {
            this.value = value;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            mode = this.value;
            if (mode == ADDRESS_MODE){
                fieldChoosePanel.setEnable(false);
            } else {
                fieldChoosePanel.setEnable(true);
            }
        }
    }

    public static void print(Demonstrator demonstrator) {
        new PrintDialog(demonstrator).setVisible(true);
    }
}
