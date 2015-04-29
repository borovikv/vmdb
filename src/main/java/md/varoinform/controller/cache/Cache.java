package md.varoinform.controller.cache;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.dao.EnterpriseDao;
import md.varoinform.model.utils.SessionManager;
import md.varoinform.util.StringUtils;
import md.varoinform.util.observer.ObservableEvent;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 6/11/14
 * Time: 10:05 AM
 */
public enum Cache implements md.varoinform.util.observer.Observer {
    instance;
    private final List<Integer> eiDs;
    private Map<Integer, Map<String, Object>> eCache = new ConcurrentHashMap<>();
    private Integer langID;
    private SwingWorker<Void, Void> worker;

    Cache() {
        eiDs = EnterpriseDao.getEIDs();
        langID = LanguageProxy.instance.getCurrentLanguage();
        worker = new CacheUpdater();
        worker.execute();
    }

    @Override
    public void update(ObservableEvent event) {
        update();
    }

    public void update() {
        if (langID.equals(LanguageProxy.instance.getCurrentLanguage())) return;

        eCache.clear();
        langID = LanguageProxy.instance.getCurrentLanguage();
        if (worker != null){
            worker.cancel(true);
        }
        worker = new CacheUpdater();
        worker.execute();

    }

    public List<Integer> getAllEnterpriseIds(){
        //return new ArrayList<>(eCache.keySet());
        return eiDs;
    }


    public Object getValue(Integer eid, Field f) {
        return getValue(eid, f.toString());
    }

    public Object getValue(Integer eid, String field){
        return StringUtils.objectOrString(getRawValue(eid, field));
    }

    public Object getRawValue(Integer eid, String field){
        Map<String, Object> proxy = eCache.get(eid);
        if (proxy != null){
            return proxy.get(field);
        } else {
            Map<String, Object> enterprisesMap = EnterpriseDao.getEnterprisesMap(eid, langID);
            if (enterprisesMap != null && !enterprisesMap.isEmpty()) {
                eCache.put(eid, enterprisesMap);
                return enterprisesMap.get(field);
            }

        }
        return null;
    }

    public Integer getCachedLanguage() {
        return langID;
    }

    public Map<String, Object> getMap(Integer eid) {
        return eCache.get(eid);
    }

    private class CacheUpdater extends SwingWorker<Void, Void> {

        @Override
        protected Void doInBackground() throws Exception {
            eCache.putAll(EnterpriseDao.getEnterprisesMap(langID));
            SessionManager.instance.clearCache();
            System.gc();
            return null;
        }
    }
}
