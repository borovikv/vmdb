package md.varoinform.controller;

import md.varoinform.model.dao.GenericDaoHibernateImpl;
import md.varoinform.model.entities.Language;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/7/13
 * Time: 11:35 AM
 */
public class LanguageProxy {
    private Language currentLanguage;
    private final GenericDaoHibernateImpl<Language, Long> languageDao;
    private static LanguageProxy instance;

    private LanguageProxy() {
        languageDao = new GenericDaoHibernateImpl<>(Language.class);
        currentLanguage = getLanguages().get(0);
    }

    public List<Language> getLanguages(){
        return languageDao.getAll();
    }

    public Language getCurrentLanguage() {
        return currentLanguage;
    }

    public String getCurrentLanguageTitle() {
        return currentLanguage.getTitle();
    }

    public void setCurrentLanguage(Language currentLanguage) {
        this.currentLanguage = currentLanguage;
    }


    public static LanguageProxy getInstance(){
        if ( instance == null ) {
            instance = new LanguageProxy();
        }
        return instance;
    }
}
