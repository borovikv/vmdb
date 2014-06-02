package md.varoinform.controller.entityproxy;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.entities.Language;
import md.varoinform.model.entities.TitleContainer;
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

    protected String getTitle(TitleContainer container){
        if ( container == null ) return "";
        return container.title(currentLanguage());
    }

    protected Language currentLanguage() {
        if (language != null) return language;
        return LanguageProxy.instance.getCurrentLanguage();
    }
}
