package md.varoinform.controller;

import md.varoinform.model.dao.LanguageDao;
import md.varoinform.util.PreferencesHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/7/13
 * Time: 11:35 AM
 */
public enum  LanguageProxy {
    instance;

    private final Map<Integer, String> languageMap;
    private Integer currentLanguage;

    LanguageProxy() {
        languageMap = LanguageDao.getLanguageMap();
        currentLanguage = new PreferencesHelper().getCurrentLanguage();
    }


    public List<Integer> getLanguages(){
        return new ArrayList<>(languageMap.keySet());
    }

    public Integer getCurrentLanguage() {
        return currentLanguage;
    }

    public static String getCurrentLanguageTitle() {
        return instance.languageMap.get(instance.currentLanguage);
    }

    public void setCurrentLanguage(Integer currentLanguage) {
        this.currentLanguage = currentLanguage;
        PreferencesHelper preferences = new PreferencesHelper();
        preferences.setCurrentLanguage(currentLanguage);
    }

    public Integer getLanguage(String title) {
        if (title == null) return null;
        for (Integer id : languageMap.keySet()) {
            String t = languageMap.get(id);
            if (t != null && t.toLowerCase().startsWith(title.toLowerCase())) return id;
        }
        return null;
    }

    public String getTitle(Integer langID) {
        return languageMap.get(langID);
    }

    public List<String> getLangTitles() {
        return new ArrayList<>(languageMap.values());
    }
}
