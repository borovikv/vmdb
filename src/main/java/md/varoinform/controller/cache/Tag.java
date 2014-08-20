package md.varoinform.controller.cache;

import md.varoinform.model.entities.Enterprise;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 8/20/14
 * Time: 9:46 AM
 */
public class Tag implements Comparable<Tag> {
    private Long id;
    private String title;
    private final List<Long> enterprises;
    private boolean isSynchronizedWithDB;

    public Tag(md.varoinform.model.entities.Tag tag) {
        id = tag.getId();
        title = tag.getTitle();
        enterprises = Collections.synchronizedList(new ArrayList<Long>());
        for (Enterprise enterprise : tag.getEnterprises()) {
            enterprises.add(enterprise.getId());
        }
        isSynchronizedWithDB = true;
    }

    public Tag(String title, List<Long> enterpriseIds) {
        id = -1L;
        this.title = title;
        this.enterprises = Collections.synchronizedList(enterpriseIds);
        isSynchronizedWithDB = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        isSynchronizedWithDB = false;
    }

    public List<Long> getEnterprises(){
        return enterprises;
    }

    public boolean remove(List<Long> enterprises){
        this.enterprises.removeAll(enterprises);
        isSynchronizedWithDB = false;
        return this.enterprises.size() <= 0;
    }

    public void add(List<Long> enterprises){
        Set<Long> set = new HashSet<>(this.enterprises);
        set.addAll(enterprises);
        this.enterprises.clear();
        this.enterprises.addAll(set);
        isSynchronizedWithDB = false;
    }

    public boolean isSynchronizedWithDB() {
        return isSynchronizedWithDB;
    }

    public void setSynchronizedWithDB(boolean isSynchronized) {
        this.isSynchronizedWithDB = isSynchronized;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Tag && id.equals(((Tag)obj).id);
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public int compareTo(Tag o) {
        if (o == null) return 1;
        if (this.equals(o)) return 0;
        return title.compareTo(o.title);
    }
}
