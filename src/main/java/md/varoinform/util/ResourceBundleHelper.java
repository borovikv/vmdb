package md.varoinform.util;

import md.varoinform.controller.LanguageProxy;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class ResourceBundleHelper implements Serializable {
    private static Map<String, ResourceBundle> bundleMap = new HashMap<>();

    private ResourceBundleHelper() {
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
        ResourceBundle bundle;
        try {
            bundle = getResourceBundle(language);
        } catch (MissingResourceException e) {
            return defaultValue;
        }

        if ( bundle.containsKey(key)){
            String val = bundle.getString(key);
            try {
                return new String(val.getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
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