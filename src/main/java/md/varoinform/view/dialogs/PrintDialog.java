package md.varoinform.view.dialogs;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.entities.Language;
import md.varoinform.util.ResourceBundleHelper;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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

    public static void main(String[] args) {
        PrintDialog pd = new PrintDialog();
        pd.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pd.setVisible(true);
    }
    public PrintDialog() {
        setSize(450, 500);
        setTitle(ResourceBundleHelper.getString("print", "Print"));

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


        JPanel buttonPanel = new JPanel();
        JButton printButton = new JButton(ResourceBundleHelper.getString("print", "Print"));
        JButton previewButton = new JButton(ResourceBundleHelper.getString("print-preview", "Preview"));
        JButton settingsButton = new JButton(ResourceBundleHelper.getString("print-settings", "Advanced"));
        buttonPanel.add(settingsButton);
        buttonPanel.add(previewButton);
        buttonPanel.add(printButton);

        add(panel, BorderLayout.CENTER);
        JScrollPane choosePane = new JScrollPane(fieldChoosePanel);
        fieldChoosePanel.setBorder(getTitledBorder(ResourceBundleHelper.getString("fields", "Fields")));
        add(choosePane, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);

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
