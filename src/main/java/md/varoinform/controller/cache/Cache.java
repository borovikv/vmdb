package md.varoinform.controller.cache;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.dao.EnterpriseDao;
import md.varoinform.model.entities.Language;
import md.varoinform.util.StringUtils;
import md.varoinform.util.observer.ObservableEvent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 6/11/14
 * Time: 10:05 AM
 */
public enum Cache implements md.varoinform.util.observer.Observer {
    instance;

    private Map<Long, Map<String, Object>> eCache = new LinkedHashMap<>();
    private Language language;

    private Cache() {
        update();
    }

    @Override
    public void update(ObservableEvent event) {
        update();
    }

    public void update() {
        eCache.clear();
        language = LanguageProxy.instance.getCurrentLanguage();
        eCache = EnterpriseDao.getEnterprisesMap(language);
    }

    public List<Long> getAllEnterpriseIds(){
        return new ArrayList<>(eCache.keySet());
    }


    public Object getValue(Long eid, Field f) {
        return getValue(eid, f.toString());
    }

    public Object getValue(Long eid, String field){
        return StringUtils.objectOrString(getRawValue(eid, field));
    }

    public Object getRawValue(Long eid, String field){
        Map<String, Object> proxy = eCache.get(eid);
        if (proxy != null){
            return proxy.get(field);
        }
        return null;
    }

    public Language getCachedLanguage() {
        return language;
    }

    public Map<String, Object> getMap(Long eid) {
        return eCache.get(eid);
    }
}
