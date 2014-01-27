package md.varoinform.util;

import md.varoinform.controller.LanguageProxy;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class ResourceBundleHelper implements Serializable {
    private static Map<String, ResourceBundle> bundleMap = new HashMap<>();

    private ResourceBundleHelper() {
    }

    public static ResourceBundle getResourceBundle() {
        return getResourceBundle(getCurrentLanguage());
    }

    private static String getCurrentLanguage() {
        String languageTitle = LanguageProxy.getCurrentLanguageTitle();
        return getLanguageTitle(languageTitle);
    }

    private static String getLanguageTitle(String languageTitle) {
        return languageTitle.substring(0, 2);
    }

    private static ResourceBundle getResourceBundle(String language) {
        language = getLanguageTitle(language);
        if (bundleMap.containsKey(language)) {
            return bundleMap.get(language);
        }
        Locale locale = new Locale(language);
        ResourceBundle bundle = ResourceBundle.getBundle("i18n.Strings", locale);

        bundleMap.put(language, bundle);

        return bundle;
    }



    public static String getString(String language, String key, String defaultValue){
        language = getLanguageTitle(language);
        ResourceBundle bundle = getResourceBundle(language);

        if ( bundle.containsKey(key)){
            return bundle.getString(key);
        }

        return defaultValue;
    }


    public static String getString(String key, String defaultValue){
        return getString(getCurrentLanguage(), key, defaultValue);
    }


    public static String getString(String key){
        return getString(key, "");
    }
}