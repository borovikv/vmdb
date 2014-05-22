package md.varoinform.view.status;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.entities.Language;
import md.varoinform.util.observer.Observable;
import md.varoinform.util.observer.ObservableEvent;
import md.varoinform.util.observer.ObservableIml;
import md.varoinform.util.observer.Observer;
import md.varoinform.view.LanguageComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 5/16/14
 * Time: 2:52 PM
 */
public enum StatusBar  implements Observable {
    instance;
    private ObservableIml observable = new ObservableIml();

    private JPanel statusBar;

    StatusBar() {
        statusBar = new JPanel();
        statusBar.setLayout(new BorderLayout());
        statusBar.add(OutputLabel.instance.getLabel(), BorderLayout.WEST);

        final LanguageComboBox languageCombo = new LanguageComboBox();
        languageCombo.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Language newLanguage = (Language)languageCombo.getSelectedItem();
                LanguageProxy.instance.setCurrentLanguage(newLanguage);
                notifyObservers(new ObservableEvent(ObservableEvent.Type.LANGUAGE_CHANGED));
            }
        });
        statusBar.add(languageCombo, BorderLayout.EAST);
    }

    public Component getStatusBar() {
        return statusBar;
    }

    @Override
    public void addObserver(Observer observer) {
        observable.addObserver(observer);
    }

    @Override
    public void notifyObservers(ObservableEvent event) {
        observable.notifyObservers(event);
    }

}
