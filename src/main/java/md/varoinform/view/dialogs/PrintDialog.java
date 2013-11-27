package md.varoinform.view.dialogs;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.entities.Language;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.view.demonstrator.Demonstrator;
import md.varoinform.view.dialogs.preview.Address;
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
    private static final int PRINT_ALL = 1;
    private static final int PRINT_SELECTED = 2;

    private int mode = DATA_MODE;
    private int amount = PRINT_SELECTED;
    private Language language;
    private final FieldChoosePanel fieldChoosePanel = new FieldChoosePanel();
    private final HashPrintRequestAttributeSet attributes;
    private PageFormat pageFormat;
    private Demonstrator demonstrator;


    public PrintDialog(Component parent, Demonstrator demonstrator) {
        this.demonstrator = demonstrator;
        setSize(450, 500);
        setTitle(ResourceBundleHelper.getString("print", "Print"));
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        JRadioButton typeAddress = new JRadioButton(ResourceBundleHelper.getString("type-address", "Address"));
        typeAddress.addActionListener(new PrintModeAction(ADDRESS_MODE));
        JRadioButton typeData = new JRadioButton(ResourceBundleHelper.getString("type-data", "Data"));
        typeData.addActionListener(new PrintModeAction(DATA_MODE));
        typeData.setSelected(true);
        JPanel typePanel = createRadioButtonGroup("print value", new JRadioButton[]{typeData, typeAddress});
        panel.add(typePanel);


        JRadioButton allCheckBox = new JRadioButton(ResourceBundleHelper.getString("all", "All"));
        allCheckBox.addActionListener(new PrintAmountAction(PRINT_ALL));
        JRadioButton selectedCheckBox = new JRadioButton(ResourceBundleHelper.getString("selected-only", "Only selected"));
        selectedCheckBox.addActionListener(new PrintAmountAction(PRINT_SELECTED));
        selectedCheckBox.setSelected(true);
        JPanel countPanel = createRadioButtonGroup("print", new JRadioButton[]{selectedCheckBox, allCheckBox});
        panel.add(countPanel);


        JPanel languagePanel = new JPanel();
        languagePanel.setBorder(getTitledBorder(ResourceBundleHelper.getString("language-choose", "Choose language")));
        List<Language> languages = LanguageProxy.getInstance().getLanguages();
        JComboBox<Object> languageCombo = new JComboBox<>(languages.toArray());
        language = LanguageProxy.getInstance().getCurrentLanguage();
        languageCombo.setSelectedIndex(languages.indexOf(language));
        languageCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED){
                    language = (Language)e.getItem();
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

        JButton pageSetupButton = new JButton(ResourceBundleHelper.getString("print-settings", "Advanced"));
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
        fieldChoosePanel.setBorder(getTitledBorder(ResourceBundleHelper.getString("fields", "Fields")));
        add(choosePane, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);

    }

    private Book makeBook() {
        if (pageFormat == null){
            PrinterJob job = PrinterJob.getPrinterJob();
            pageFormat = job.defaultPage();
        }

        Book book = new Book();
        List<Enterprise> enterprises = null;
        if (amount == PRINT_ALL) {
            enterprises = demonstrator.getALL();
        } else if (amount == PRINT_SELECTED) {
            enterprises = demonstrator.getSelected();
        }

        if (mode == DATA_MODE){

        } else if (mode == ADDRESS_MODE){
            Address address = new Address(enterprises);

            int pageCount = address.getPageCount(pageFormat);
            book.append(address, pageFormat, pageCount);
        }

        return book;
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
        return BorderFactory.createTitledBorder( title);
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

    private class PrintAmountAction extends AbstractAction {

        private final int value;

        private PrintAmountAction(int value) {
            this.value = value;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            amount = this.value;
        }
    }
}
