package md.varoinform.controller;

import md.varoinform.controller.entityproxy.EnterpriseProxy;
import md.varoinform.model.dao.DAOTag;
import md.varoinform.model.dao.EnterpriseDao;
import md.varoinform.model.dao.NodeDao;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.entities.Node;
import md.varoinform.model.entities.Tag;
import md.varoinform.util.StringUtils;
import md.varoinform.util.observer.*;

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

    @Override
    public void update(ObservableEvent event) {
        proxyCache = getEnterpriseProxies(enterpriseCache.values());
        tableCache = getTableValues(proxyCache);
    }

    private final Map<Long, Enterprise> enterpriseCache = new LinkedHashMap<>();
    private final Map<Long, List<Long>> branchCache = new HashMap<>();

    private Map<Long, EnterpriseProxy> proxyCache = new HashMap<>();
    private Map<Long, Map<String, Object>> tableCache = new HashMap<>();

    private final Set<Tag> tags = new TreeSet<>();
    private final DAOTag daoTag = new DAOTag();

    private Cache() {
        List<Enterprise> enterprises = EnterpriseDao.getEnterprises();
        for (Enterprise enterprise : enterprises) {
            Long id = enterprise.getId();
            enterpriseCache.put(id, enterprise);
        }

        proxyCache = getEnterpriseProxies(enterprises);
        tableCache = getTableValues(proxyCache);

        NodeDao nodeDao = new NodeDao();
        List<Node> nodes = nodeDao.getAll();
        for (Node node : nodes) {
            List<Long> ids = nodeDao.getEnterpriseIds(node);
            branchCache.put(node.getId(), ids);
        }

        tags.addAll(daoTag.getAll());
    }

    private Map<Long, Map<String, Object>> getTableValues(Map<Long, EnterpriseProxy> proxyCache) {
        List<String> fields = EnterpriseProxy.getFields();
        Map<Long, Map<String, Object>> tableCache = new HashMap<>();
        for (Long id : proxyCache.keySet()) {
            Map<String, Object> values = new HashMap<>();
            for (String field : fields) {
                Object value = proxyCache.get(id).get(field);
                values.put(field, StringUtils.objectOrString(value));
            }
            tableCache.put(id, values);
        }
       return tableCache;
    }

    private Map<Long, EnterpriseProxy> getEnterpriseProxies(Collection<Enterprise> enterprises) {
        Map<Long, EnterpriseProxy> proxies = new HashMap<>(enterprises.size());
        for (Enterprise enterprise : enterprises) {
            proxies.put(enterprise.getId(), new EnterpriseProxy(enterprise));

        }
        return proxies;
    }

    public List<Enterprise> getEnterprises(List<Long> ids){
        List<Enterprise> result = new ArrayList<>();
        for (Long id : ids) {
            Enterprise enterprise = enterpriseCache.get(id);
            if (enterprise != null){
                result.add(enterprise);
            }
        }
        return result;
    }

    public List<Enterprise> getAllEnterprises(){
        return new ArrayList<>(enterpriseCache.values());
    }

    public List<Enterprise> getEnterpriseByNode(Node node){
        List<Long> ids = branchCache.get(node.getId());
        if (ids == null) return new ArrayList<>();
        return getEnterprises(ids);
    }

    public EnterpriseProxy getProxy(Enterprise enterprise){
        EnterpriseProxy proxies = proxyCache.get(enterprise.getId());
        if (proxies != null){
            return proxies;
        }
        return null;
    }

    public List<Tag> getTags() {
        return new ArrayList<>(tags);
    }

    public void saveTag(final Tag tag){
        new SwingWorker<Void, Void>(){

            @Override
            protected Void doInBackground() throws Exception {
                daoTag.save(tag);
                return null;
            }
        }.execute();
    }

    public void createTag(String title, List<Enterprise> enterprises) {
        Tag tag = daoTag.createTag(title, enterprises);
        if (tag == null) return;
        tags.add(tag);
        saveTag(tag);
    }

    public void delete(final Tag tag) {
        tags.remove(tag);
        new SwingWorker<Void, Void>(){

            @Override
            protected Void doInBackground() throws Exception {
                daoTag.delete(tag);
                return null;
            }
        }.execute();
    }


    public Object getValue(Enterprise enterprise, String field){
        Map<String, Object> proxyValues = tableCache.get(enterprise.getId());
        if (proxyValues != null){
            return proxyValues.get(field);
        }
        return null;
    }
}
