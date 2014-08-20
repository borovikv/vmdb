package md.varoinform.controller.cache;

import md.varoinform.controller.Holder;
import md.varoinform.model.dao.DAOTag;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 8/13/14
 * Time: 5:49 PM
 */

public enum TagCache {
    instance;
    private final Set<Tag> tags = new ConcurrentSkipListSet<>();

    TagCache() {
        update();
    }

    public void update(){
        tags.clear();
        tags.addAll(new DAOTag().getAll());
    }

    public List<Tag> getTags() {
        return new ArrayList<>(tags);
    }


    public void addTag(String title, List<Long> enterpriseIds) {
        for (Tag tag : tags) {
            if (tag.getTitle().equals(title)){
                tag.add(enterpriseIds);
            } else {
                Tag newTag = new Tag(title, enterpriseIds);
                tags.add(newTag);
            }
        }
        synchronizeWithDB();
    }

    public void delete(Tag tag) {
        tags.remove(tag);
        new DAOTag().delete(tag);
    }

    public void synchronizeWithDB(){
        try (Holder ignored = new Holder()){
            for (Tag t : tags) {
                if (t.getEnterprises().isEmpty()){
                    delete(t);
                }
            }
            new DAOTag().synchronizeWithDB(tags);
        }
    }

    @SuppressWarnings("UnusedParameters")
    public void updateTag(Tag tag) {
        synchronizeWithDB();
    }
}
