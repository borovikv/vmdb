package md.varoinform.view;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.entities.Language;

import javax.swing.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/3/13
 * Time: 3:20 PM
 */
public class LanguageComboBox extends JComboBox<Language> {
    private final List<Language> languages;
    private List<Language> disabledLanguages = new ArrayList<>();
    private List<Language> currentLanguages = new ArrayList<>();

    public LanguageComboBox() {
        languages = LanguageProxy.instance.getLanguages();
        setCurrentLanguages();

    }

    public void setCurrentLanguages() {
        currentLanguages = new ArrayList<>(languages);
        for (Language language : disabledLanguages) {
            currentLanguages.remove(language);
        }
        DefaultComboBoxModel<Language> model = new DefaultComboBoxModel<>(currentLanguages.toArray(new Language[languages.size()]));
        Language currentLanguage = LanguageProxy.instance.getCurrentLanguage();
        if (currentLanguages.contains(currentLanguage)) {
            model.setSelectedItem(currentLanguage);
        }
        removeAllItems();
        setModel(model);
    }

    public void setEnableItem(Language language, boolean enable){
        if (language == null) return;
        if (languages.contains(language)){
            if (enable){
                disabledLanguages.remove(language);
            } else if (!disabledLanguages.contains(language)){
                disabledLanguages.add(language);
            }
            setCurrentLanguages();
        }
    }

    @Override
    public void setSelectedIndex(int anIndex) {
        if (disabledLanguages.contains(languages.get(anIndex))) return;
        super.setSelectedIndex(anIndex);
    }
}
