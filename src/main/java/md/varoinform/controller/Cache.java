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
        enterpriseProxies.clear();
        List<Enterprise> enterprises = EnterpriseDao.getEnterprises();
        for (Enterprise enterprise : enterprises) {
            enterpriseProxies.put(enterprise.getId(), new EnterpriseProxy(enterprise));
        }
    }

    private final Map<Long, EnterpriseProxy> enterpriseProxies = new LinkedHashMap<>();
    private final Map<Long, List<Long>> branchCache = new HashMap<>();


    private final Set<Tag> tags = new TreeSet<>();
    private final DAOTag daoTag = new DAOTag();

    private Cache() {
        List<Enterprise> enterprises = EnterpriseDao.getEnterprises();

        for (Enterprise enterprise : enterprises) {
            Long id = enterprise.getId();
            //enterpriseCache.put(id, enterprise);
            enterpriseProxies.put(id, new EnterpriseProxy(enterprise));
        }
        createBranchCache();

        tags.addAll(daoTag.getAll());
    }

    private void createBranchCache() {
        NodeDao nodeDao = new NodeDao();
        List<Node> nodes = nodeDao.getAll();
        for (Node node : nodes) {
            List<Long> ids = nodeDao.getEnterpriseIds(node);
            branchCache.put(node.getId(), ids);
        }
    }

    public List<Long> getAllEnterpriseIds(){
        return new ArrayList<>(enterpriseProxies.keySet());
    }

    public List<Long> getEnterpriseIdByNode(Node node){
        List<Long> ids = branchCache.get(node.getId());
        if (ids == null) return new ArrayList<>();
        return ids;
    }

    public List<Tag> getTags() {
        return new ArrayList<>(tags);
    }

    public void saveTag(final Tag tag, List<Enterprise> enterprises){
        tag.getEnterprises().addAll(enterprises);
        saveTag(tag);
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


    public Object getValue(Long enterprise, String field){
        EnterpriseProxy proxy = enterpriseProxies.get(enterprise);
        if (proxy != null){
            return StringUtils.objectOrString(proxy.get(field));
        }
        return null;
        //return StringUtils.objectOrString(new EnterpriseProxy(enterprise).get(field));
    }

    public EnterpriseProxy getProxy(long id) {
        return enterpriseProxies.get(id);
    }
}
