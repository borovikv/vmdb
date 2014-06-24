package md.varoinform.controller;

import md.varoinform.controller.entityproxy.EnterpriseProxy;
import md.varoinform.model.dao.DAOTag;
import md.varoinform.model.dao.EnterpriseDao;
import md.varoinform.model.dao.NodeDao;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.entities.Node;
import md.varoinform.model.entities.Tag;
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

    @Override
    public void update(ObservableEvent event) {
        enterpriseProxies.clear();
        Collection<Enterprise> enterprises;
        if (enterpriseCache.isEmpty()){
            enterprises = EnterpriseDao.getEnterprises();
        } else {
            enterprises = enterpriseCache.values();
        }
        for (Enterprise enterprise : enterprises) {
            enterpriseProxies.put(enterprise.getId(), new EnterpriseProxy(enterprise));
        }
    }

    private final Map<Long, Enterprise> enterpriseCache = new LinkedHashMap<>();
    private final Map<Long, EnterpriseProxy> enterpriseProxies = new LinkedHashMap<>();
    private final Map<Long, List<Long>> branchCache = new HashMap<>();

    private final Set<Tag> tags = new TreeSet<>();
    private final Set<Tag> tagsToSave = new HashSet<>();
    private final Set<Tag> tagsToDelete = new HashSet<>();
    private final DAOTag daoTag = new DAOTag();
    private static final boolean isEnterpriseCached = true;
    private static final boolean isBranchCached = false;


    private Cache() {
        List<Enterprise> enterprises = EnterpriseDao.getEnterprises();

        for (Enterprise enterprise : enterprises) {
            Long id = enterprise.getId();
            enterpriseCache.put(id, enterprise);
            enterpriseProxies.put(id, new EnterpriseProxy(enterprise));
        }

        tags.addAll(daoTag.getAll());
    }


    public List<Long> getAllEnterpriseIds(){
        return new ArrayList<>(enterpriseProxies.keySet());
    }

    public List<Long> getEnterpriseIdByNode(Node node){
        if (isBranchCached){
            return getCachedEnterpriseIdsByNode(node);
        } else {
            return new NodeDao().getEnterpriseIds(node);
        }
    }

    public List<Long> getCachedEnterpriseIdsByNode(Node node) {
        if (branchCache.isEmpty() && !Holder.isWait()) {

            List<Long> ids = new NodeDao().getEnterpriseIds(node);

            new SwingWorker<Void, Void>(){
                @Override
                protected Void doInBackground() throws Exception {

                    try(Holder ignored = new Holder()){
                        createBranchCache();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();

            return ids;
        } else if (branchCache.containsKey(node.getId())){

            return branchCache.get(node.getId());
        } else {

            return new NodeDao().getEnterpriseIds(node);
        }
    }

    private void createBranchCache() {
        NodeDao nodeDao = new NodeDao();
        List<Node> nodes = nodeDao.getAll();
        for (Node node : nodes) {
            List<Long> ids = nodeDao.getEnterpriseIds(node);
            branchCache.put(node.getId(), ids);
        }
    }

    public List<Tag> getTags() {
        return new ArrayList<>(tags);
    }

    public void saveTag(Tag tag, List<Enterprise> enterprises){
        tag.getEnterprises().addAll(enterprises);
        saveTag(tag);
    }

    public void saveTag(Tag tag){
        tagsToSave.add(tag);
    }

    public void createTag(String title, List<Enterprise> enterprises) {
        Tag tag = daoTag.createTag(title, enterprises);
        if (tag == null) return;
        tags.add(tag);
        saveTag(tag);
    }

    public void delete(Tag tag) {
        tags.remove(tag);
        tagsToSave.remove(tag);
        tagsToDelete.add(tag);
    }

    public boolean deleteFromTag(Tag tag, List<Long> enterpriseIds){
        List<Enterprise> enterprises = new EnterpriseDao().read(enterpriseIds);
        tag.removeAll(enterprises);
        if (tag.getEnterprises().isEmpty()) {
            delete(tag);
            return true;
        } else {
            saveTag(tag);
            return false;
        }
    }


    public Object getValue(Long enterprise, String field){
        EnterpriseProxy proxy = enterpriseProxies.get(enterprise);
        if (proxy != null){
            return StringUtils.objectOrString(proxy.get(field));
        }
        return null;
    }

    public EnterpriseProxy getProxy(long id) {
        return enterpriseProxies.get(id);
    }

    public void shutDown(){
        daoTag.delete(tagsToDelete);
        daoTag.save(tagsToSave);
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
