package md.varoinform.util;

import md.varoinform.Settings;
import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.entities.Language;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    private static ResourceBundle getResourceBundle(String language) throws MalformedURLException {
        language = getLanguageTitle(language);
        if (bundleMap.containsKey(language)) {
            return bundleMap.get(language);
        }
        Locale locale = new Locale(language);

        Path path = Paths.get(Settings.getWorkFolder(), "external-resources");
        URL[] urls = { path.toUri().toURL()};
        ClassLoader classLoader = new URLClassLoader(urls);

        ResourceBundle bundle = ResourceBundle.getBundle("i18n.Strings", locale, classLoader);

        bundleMap.put(language, bundle);

        return bundle;
    }



    public static String getString(String language, String key, String defaultValue){
        language = getLanguageTitle(language);
        ResourceBundle bundle;
        try {
            bundle = getResourceBundle(language);
        } catch (MissingResourceException|MalformedURLException e) {
            e.printStackTrace();
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

    public static String getString(Language language, String key, String defaultValue) {
        return getString(language.getTitle(), key, defaultValue);
    }
}