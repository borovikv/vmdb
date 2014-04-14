package md.varoinform.controller.entityproxy;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.entities.Language;
import md.varoinform.model.entities.TitleContainer;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/13/13
 * Time: 1:51 PM
 */
public class EntityProxy {

    private Language language;

    public EntityProxy() {
    }

    public EntityProxy(Language language) {
        this.language = language;
    }



    protected String getStringValueOrEmpty(Object value) {
        if ( value == null || value instanceof String && ((String) value).isEmpty() )
            return  "";
        if (value instanceof Date) {
            return new SimpleDateFormat("yyyy-MM-dd").format(value);
        }
        return value.toString();
    }

    protected String getTitle(TitleContainer container){
        if ( container == null ) return "";
        return container.title(currentLanguage());
    }

    protected Language currentLanguage() {
        if (language != null) return language;
        return LanguageProxy.instance.getCurrentLanguage();
    }
}
