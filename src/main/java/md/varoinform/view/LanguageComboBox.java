package md.varoinform.view;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.entities.Language;
import md.varoinform.util.Observable;
import md.varoinform.util.ObservableEvent;
import md.varoinform.util.Observer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/3/13
 * Time: 3:20 PM
 */
public class LanguageComboBox extends JComboBox<Language> implements Observable {
    private List<Observer> observers = new ArrayList<>();

    public LanguageComboBox() {
        DefaultComboBoxModel<Language> model = new DefaultComboBoxModel<>(getLanguages());
        model.setSelectedItem(LanguageProxy.instance.getCurrentLanguage());
        setModel(model);
        addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Language newLanguage = (Language) getSelectedItem();
                LanguageProxy.instance.setCurrentLanguage(newLanguage);
                notifyObservers(new ObservableEvent(ObservableEvent.LANGUAGE_CHANGED));
            }
        });
    }

    private Language[] getLanguages() {
        List<Language> languageList = LanguageProxy.instance.getLanguages();
        Language[] languages = new Language[languageList.size()];
        languageList.toArray(languages);
        return languages;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers(ObservableEvent event) {
        for (Observer observer : observers) {
            observer.update(event);
        }
    }
}
