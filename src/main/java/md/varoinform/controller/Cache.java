package md.varoinform.controller;

import md.varoinform.controller.entityproxy.EnterpriseProxy;
import md.varoinform.model.dao.EnterpriseDao;
import md.varoinform.model.dao.NodeDao;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.entities.Node;
import md.varoinform.util.StringUtils;
import md.varoinform.util.observer.ObservableEvent;

import javax.swing.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 6/11/14
 * Time: 10:05 AM
 */
public enum Cache implements md.varoinform.util.observer.Observer {
    instance;
    private static final boolean isEnterpriseCached = true;
    private static final boolean isBranchCached = true;
    private final Map<Long, Enterprise> enterpriseCache = new LinkedHashMap<>();
    private final Map<Long, EnterpriseProxy> enterpriseProxies = new LinkedHashMap<>();

    private Map<Long, List<Long>> branchCache = new HashMap<>();

    private Cache() {
        update();
    }

    @Override
    public void update(ObservableEvent event) {
        updateFromCache();
    }

    public void update() {
        enterpriseProxies.clear();
        branchCache.clear();

        Collection<Enterprise> enterprises = EnterpriseDao.getEnterprises();

        for (Enterprise enterprise : enterprises) {
            Long id = enterprise.getId();
            enterpriseCache.put(id, enterprise);
            enterpriseProxies.put(id, new EnterpriseProxy(enterprise));
        }


    }

    public void updateFromCache(){
        enterpriseProxies.clear();
        branchCache.clear();

        for (Enterprise enterprise : enterpriseCache.values()) {
            Long id = enterprise.getId();
            enterpriseProxies.put(id, new EnterpriseProxy(enterprise));
        }
    }

    public List<Long> getAllEnterpriseIds(){
        return new ArrayList<>(enterpriseProxies.keySet());
    }

    public List<Long> getEnterpriseIdByNode(Node node){
        if (isBranchCached){
            return getCachedEnterpriseIdsByNode(node);
        } else {
            return new NodeDao().threadSafeGetEntID(node);
        }
    }

    private List<Long> getCachedEnterpriseIdsByNode(Node node) {
        if (branchCache.isEmpty() && !Holder.await()) {
            List<Long> ids = new NodeDao().getEnterpriseIds(node);

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {

                    try (Holder ignored = new Holder()) {
                        branchCache = createBranchCache();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
            worker.execute();


            return ids;
        } else if (branchCache.containsKey(node.getId())){

            return branchCache.get(node.getId());
        } else {

            return new NodeDao().getEnterpriseIds(node);
        }
    }

    private Map<Long, List<Long>> createBranchCache() {
        NodeDao nodeDao = new NodeDao();
        List<Node> nodes = nodeDao.getAll();
        Map<Long, List<Long>> bc = new HashMap<>();
        for (Node node : nodes) {
            List<Long> ids = nodeDao.threadSafeGetEntID(node);
            bc.put(node.getId(), ids);
        }
        return bc;
    }




    public Object getValue(Long enterprise, String field){
        EnterpriseProxy proxy = enterpriseProxies.get(enterprise);
        if (proxy != null){
            return StringUtils.objectOrString(proxy.get(field));
        }
        return null;
    }

    public Object getFieldValue(Long eid, String field){
        EnterpriseProxy proxy = enterpriseProxies.get(eid);
        if (proxy != null){
            return proxy.get(field);
        }
        return null;
    }



    public Enterprise getEnterprise(Long id) {
        return enterpriseCache.get(id);
    }

    public List<Enterprise> getEnterprises(List<Long> ids) {
        List<Enterprise> result = new ArrayList<>();
        for (Long id : ids) {
            Enterprise e = enterpriseCache.get(id);
            if (e != null){
                result.add(e);
            }
        }
        return result;
    }

    public boolean isEnterpriseCached(){
        return isEnterpriseCached;
    }
}
