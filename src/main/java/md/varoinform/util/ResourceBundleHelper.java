package md.varoinform.util;

import md.varoinform.Settings;
import md.varoinform.controller.DefaultLanguages;
import md.varoinform.controller.LanguageProxy;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ResourceBundleHelper implements Serializable {
    private static final Map<Locale, ResourceBundle> bundels = new HashMap<>();
    private ResourceBundleHelper() {
    }

    private static ResourceBundle getResourceBundle(DefaultLanguages language) throws MalformedURLException {
        String languageTitle = getLanguageTitle(language.getTitle());
        Locale locale = new Locale(languageTitle);
        String name = "i18n.Strings";
        return getResourceBundle(name, locale);
    }

    private static String getLanguageTitle(String languageTitle) {
        return languageTitle.substring(0, 2);
    }

    private static ResourceBundle getResourceBundle(String name, Locale locale) throws MalformedURLException {
        if (bundels.containsKey(locale)) return bundels.get(locale);
        Path path = Paths.get(Settings.getWorkFolder(), "external-resources");
        URL[] urls = { path.toUri().toURL()};
        ClassLoader classLoader = new URLClassLoader(urls);

        ResourceBundle bundle = ResourceBundle.getBundle(name, locale, classLoader);
        bundels.put(locale, bundle);
        return bundle;
    }


    public static String getString(DefaultLanguages language, String key, String defaultValue){
        ResourceBundle bundle;
        try {
            bundle = getResourceBundle(language);
        } catch (MissingResourceException|MalformedURLException e) {
            e.printStackTrace();
            return defaultValue;
        }
        if ( bundle.containsKey(key)){
            String val = bundle.getString(key);

            if (val.isEmpty()) return defaultValue;
            try {
                return new String(val.getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return defaultValue;
    }

    /*
    get string by key for current language
    if key not found return defaultValue
     */
    public static String getString(String key, String defaultValue){
        return getString(getCurrentLanguage(), key, defaultValue);
    }

    private static DefaultLanguages getCurrentLanguage() {
        String languageTitle = LanguageProxy.getCurrentLanguageTitle();
        return DefaultLanguages.getLanguageByTitle(languageTitle);
    }

    public static String getStringForUserLocale(String key, String defaultValue){
        String userLanguage = System.getProperty("user.language");
        return getString(DefaultLanguages.getLanguageByTitle(userLanguage), key, defaultValue);
    }

    /*
    get string with empty default value
     */
    public static String getString(String key){
        return getString(key, "");
    }

    /*
    get string by Language:language and key
    if key not found return default value
     */
    public static String getString(Integer langID, String key, String defaultValue) {
        return getString(DefaultLanguages.language(langID), key, defaultValue);
    }

    /*
    get string by key from bundle = bandleName
     */
    public static String getStringFromBundle(String bundleName, String key){
        try {
            ResourceBundle resourceBundle = getResourceBundle(bundleName, Locale.getDefault());

            return resourceBundle.getString(key);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getStringFromBundle(String bundleName, String key, String def){
        String result = getStringFromBundle(bundleName, key);
        if (result == null) result = def;
        return result;
    }
}