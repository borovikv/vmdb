package md.varoinform.view;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.entities.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/30/13
 * Time: 11:18 AM
 */
public class StatusBar extends JPanel{
    private JLabel outputLabel;
    private LanguageProxy languageProxy;
    private JComboBox comboBox;

    public StatusBar( LanguageProxy proxy ) {
        languageProxy = proxy;

        setBorder(BorderFactory.createEtchedBorder());
        setLayout(new BorderLayout());
        outputLabel = new JLabel();
        outputLabel.setText("result: 0");
        add(outputLabel, BorderLayout.WEST);
        addComboBox();
    }

    private void addComboBox() {
        comboBox = new JComboBox(languageProxy.getLanguages().toArray());
        comboBox.addActionListener( new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                languageProxy.setCurrentLanguage((Language)comboBox.getSelectedItem());
            }
        });
        add(comboBox, BorderLayout.EAST);
    }

    public void setResult(int resultCount){
        outputLabel.setText("result: " + resultCount);
    }

}
