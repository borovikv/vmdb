package md.varoinform.controller;

import md.varoinform.model.dao.GenericDaoHibernateImpl;
import md.varoinform.model.entities.Language;
import org.apache.commons.lang.ObjectUtils;

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
    private final GenericDaoHibernateImpl<Language, Long> languageDao;

    private LanguageProxy() {
        languageDao = new GenericDaoHibernateImpl<>(Language.class);
        List<Language> languages = getLanguages();
        if (languages.size() > 0) {
            currentLanguage = languages.get(0);
        }
    }

    public List<Language> getLanguages(){
        return languageDao.getAll();
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
    }
}
