package md.varoinform.view;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.controller.ObservableEvent;
import md.varoinform.model.entities.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/30/13
 * Time: 11:18 AM
 */
public class StatusBar extends JPanel implements Observer {
    private JLabel outputLabel;
    private LanguageProxy languageProxy;
    private JComboBox comboBox;
    private int result = 0;

    public StatusBar( LanguageProxy proxy ) {
        languageProxy = proxy;

        setBorder(BorderFactory.createEtchedBorder());
        setLayout(new BorderLayout());
        outputLabel = new JLabel();
        setResult(0);
        add(outputLabel, BorderLayout.WEST);
        addComboBox();
    }

    private void addComboBox() {
        comboBox = new JComboBox(languageProxy.getLanguages().toArray());
        comboBox.addActionListener( new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                languageProxy.setCurrentLanguage((Language) comboBox.getSelectedItem());
            }
        });
        add(comboBox, BorderLayout.EAST);
    }

    public void setResult(int resultCount){
        result = resultCount;
        updateDisplay();
    }

    private void updateDisplay(){
        Locale locale = new Locale(LanguageProxy.getInstance().getCurrentLanguage().getTitle());
        ResourceBundle bundle = ResourceBundle.getBundle("i18n.Strings", locale);
        String message = bundle.getString("result") + ": " + result;
        outputLabel.setText( message );
    }

    @Override
    public void update(Observable o, Object arg) {
        if ( ((ObservableEvent)arg).getType() == ObservableEvent.LANGUAGE_CHANGED ) {
            updateDisplay();
        }
    }
}
