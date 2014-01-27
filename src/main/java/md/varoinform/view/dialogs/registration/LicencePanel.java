package md.varoinform.view.dialogs.registration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/18/13
 * Time: 1:50 PM
 */
public class LicencePanel extends CardPanel {
    private Observable observable = new Observable(){
        @Override
        public void notifyObservers(Object arg) {
            setChanged();
            super.notifyObservers(arg);
        }
    };
    private JCheckBox checkBox;
    private JComboBox<String> languageCombo = new  JComboBox<>(TranslateHelper.getLanguages());
    private static final String CHECKBOX_KEY = "i_agree";

    public LicencePanel() {
        super("licence");
        setLayout(new BorderLayout());
        add(new JScrollPane(label));

        JPanel panel = new JPanel(new BorderLayout());
        checkBox = new JCheckBox(getText(CHECKBOX_KEY, "i agree"));
        panel.add(checkBox, BorderLayout.WEST);

        panel.add(languageCombo, BorderLayout.EAST);
        languageCombo.setSelectedItem(TranslateHelper.instance.getCurrentLanguage());
        languageCombo.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = (String) languageCombo.getSelectedItem();
                TranslateHelper.instance.setCurrentLanguage(selectedItem);
                notifyObservers(selectedItem);
            }
        });

        add(panel, BorderLayout.SOUTH);
    }

    @Override
    public boolean isInputValid(){
        return checkBox.isSelected();
    }

    @Override
    protected void updateDisplay() {
        checkBox.setText(getText(CHECKBOX_KEY, "i agree"));
    }

    public void addCheckBoxListener(ActionListener actionListener) {
        checkBox.addActionListener(actionListener);
    }

    public void addObserver(java.util.Observer observer) {
        observable.addObserver(observer);
    }

    private void notifyObservers(Object arg) {
        observable.notifyObservers(arg);

    }


}
