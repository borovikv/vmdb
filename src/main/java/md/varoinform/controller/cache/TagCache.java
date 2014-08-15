package md.varoinform.controller.cache;

import md.varoinform.model.dao.DAOTag;
import md.varoinform.model.dao.EnterpriseDao;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.entities.Tag;
import md.varoinform.model.util.SessionManager;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 8/13/14
 * Time: 5:49 PM
 */
public enum TagCache {
    instance;

    private final Set<Tag> tags = new TreeSet<>();
    private final Set<Tag> tagsToSave = new HashSet<>();
    private final Set<Tag> tagsToDelete = new HashSet<>();
    private final DAOTag daoTag = new DAOTag();

    TagCache() {
        update();
    }

    public void update(){
        tags.clear();
        tags.addAll(daoTag.getAll());
    }

    public List<Tag> getTags() {
        return new ArrayList<>(tags);
    }

    public void saveTag(Tag tag, List<Long> eids){
        List<Enterprise> enterprises = new EnterpriseDao().read(eids);
        tag.getEnterprises().addAll(enterprises);
        saveTag(tag);
    }

    public void saveTag(Tag tag){
        tagsToSave.add(tag);
        SessionManager.instance.getSession().evict(tag);
    }

    public void createTag(String title, List<Long> eids) {
        List<Enterprise> enterprises = new EnterpriseDao().read(eids);
        Tag tag = daoTag.createTag(title, enterprises);
        if (tag == null) return;
        tags.add(tag);
        saveTag(tag);
    }

    public void delete(Tag tag) {
        tags.remove(tag);
        SessionManager.instance.getSession().evict(tag);
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

    public void shutDown(){
        daoTag.delete(tagsToDelete);
        daoTag.save(tagsToSave);
    }
}
