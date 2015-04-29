package md.varoinform.controller.entityproxy;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.entities.base.TitleContainer;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/13/13
 * Time: 1:51 PM
 */
public class EntityProxy {

    private final Integer langID;

    public EntityProxy() {
        langID = LanguageProxy.instance.getCurrentLanguage();
    }

    public EntityProxy(Integer langID) {
        this.langID = langID;
    }

    protected String getTitle(TitleContainer container){
        if ( container == null ) return "";
        return container.title(LanguageProxy.instance.getTitle(langID));
    }

    public Integer getLangID() {
        return langID;
    }
}
