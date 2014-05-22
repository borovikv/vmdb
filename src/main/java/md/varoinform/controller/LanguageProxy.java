package md.varoinform.controller;

import md.varoinform.model.dao.GenericDaoHibernateImpl;
import md.varoinform.model.entities.Language;
import md.varoinform.util.PreferencesHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/7/13
 * Time: 11:35 AM
 */
public enum  LanguageProxy {
    instance;

    private Language currentLanguage;
    private List<Language> languages;

    private LanguageProxy() {
        languages = getLanguages();
        if (languages.size() > 0) {
            currentLanguage = getPrefLanguage(languages);
        }
    }

    private Language getPrefLanguage(List<Language> languages) {
        PreferencesHelper preferences = new PreferencesHelper();
        String prefLanguage = preferences.getCurrentLanguage();
        for (Language language : languages) {
            if (language.getTitle().startsWith(prefLanguage)){
                return language;
            }
        }
        return languages.get(0);
    }

    public List<Language> getLanguages(){
        if (languages != null)
            return new ArrayList<>(languages);
        GenericDaoHibernateImpl<Language, Long> languageDao = new GenericDaoHibernateImpl<>(Language.class);
        languages = languageDao.getAll();
        return new ArrayList<>(languages);
    }

    public Language getCurrentLanguage() {
        return currentLanguage;
    }

    public static String getCurrentLanguageTitle() {
        Language curLang = instance.getCurrentLanguage();
        if (curLang != null)
            return curLang.getTitle();
        return "en";
    }

    public void setCurrentLanguage(Language currentLanguage) {
        this.currentLanguage = currentLanguage;
        PreferencesHelper preferences = new PreferencesHelper();
        preferences.setCurrentLanguage(currentLanguage.getTitle());
    }

    public Language getLanguage(String title) {
        if (title == null) return null;
        List<Language> list = getLanguages();
        for (Language language : list) {
            if (language.getTitle().toLowerCase().startsWith(title.toLowerCase())){
                return language;
            }
        }
        return null;
    }
}
