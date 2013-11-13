package md.varoinform.controller;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/13/13
 * Time: 2:46 PM
 */
public class EntityView {
    protected static ResourceBundle getResourceBundle() {
        Locale locale = new Locale(LanguageProxy.getInstance().getCurrentLanguageTitle());
        return ResourceBundle.getBundle("i18n.Strings", locale);
    }

    protected static String getStringOrNA(String s){
        return s.isEmpty() ?  getResourceBundle().getString("null") : s;
    }
}
