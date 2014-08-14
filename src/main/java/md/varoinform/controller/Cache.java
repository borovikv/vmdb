package md.varoinform.controller;

import md.varoinform.controller.entityproxy.EnterpriseProxy;
import md.varoinform.model.dao.EnterpriseDao;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.util.ClosableSession;
import md.varoinform.util.StringUtils;
import md.varoinform.util.observer.ObservableEvent;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 6/11/14
 * Time: 10:05 AM
 */
public enum Cache implements md.varoinform.util.observer.Observer {
    instance;

    private final Map<Long, Map<String, Object>> eCache = new LinkedHashMap<>();

    private Cache() {
        update();
    }

    @Override
    public void update(ObservableEvent event) {
        update();
    }

    public void update() {
        eCache.clear();
        try(ClosableSession session = new ClosableSession()) {
            Collection<Enterprise> enterprises = EnterpriseDao.getEnterprises(session);
            for (Enterprise enterprise : enterprises) {
                Long id = enterprise.getId();
                Map<String, Object> cache = createEnterpriseCache(enterprise);
                eCache.put(id, cache);
            }
        }
        System.gc();
    }

    private Map<String, Object> createEnterpriseCache(Enterprise enterprise) {
        Map<String, Object> map = new HashMap<>();
        EnterpriseProxy proxy = new EnterpriseProxy(enterprise);
        for (String field : EnterpriseProxy.getFields()) {
            Object value = proxy.get(field);
            map.put(field, value);
        }
        return map;
    }

    public List<Long> getAllEnterpriseIds(){
        return new ArrayList<>(eCache.keySet());
    }



    public Object getValue(Long eid, String field){
        Map<String, Object> proxy = eCache.get(eid);
        if (proxy != null){
            return StringUtils.objectOrString(proxy.get(field));
        }
        return null;
    }

    public Object getFieldValue(Long eid, String field){
        Map<String, Object> proxy = eCache.get(eid);
        if (proxy != null){
            return proxy.get(field);
        }
        return null;
    }


    public Enterprise getEnterprise(Long id) {
        return new EnterpriseDao().read(id);
    }

}
