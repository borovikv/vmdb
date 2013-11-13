package md.varoinform.controller;

import md.varoinform.model.entities.Language;
import md.varoinform.model.entities.TitleContainer;

import java.util.Collection;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/13/13
 * Time: 1:51 PM
 */
public class EntityProxy {

    protected ResourceBundle getResourceBundle() {
        Locale locale = new Locale(LanguageProxy.getInstance().getCurrentLanguageTitle());
        return ResourceBundle.getBundle("i18n.Strings", locale);
    }

    protected String getStringValueOrEmpty(Object value) {
        if ( value == null || value != null && value instanceof CharSequence && ((CharSequence) value).length() == 0 )
            return  "";
        return value.toString();
    }

    protected String getTitle(TitleContainer container){
        if ( container == null ) return "";
        return container.title(currentLanguage());
    }

    private Language currentLanguage() {
        return LanguageProxy.getInstance().getCurrentLanguage();
    }
}
