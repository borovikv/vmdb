package md.varoinform.controller.cache;


import md.varoinform.model.entities.enterprise.Enterprise;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 8/20/14
 * Time: 9:46 AM
 */
public class Tag implements Comparable<Tag> {
    private Integer id;
    private String title;
    private final List<Integer> enterprises;
    private boolean isSynchronizedWithDB;

    public Tag(md.varoinform.model.entities.local.Tag tag) {
        id = tag.getId();
        title = tag.getTitle();
        enterprises = Collections.synchronizedList(new ArrayList<Integer>());
        for (Enterprise enterprise : tag.getEnterprises()) {
            enterprises.add(enterprise.getId());
        }
        isSynchronizedWithDB = true;
    }

    public Tag(String title, List<Integer> enterpriseIds) {
        id = -1;
        this.title = title;
        this.enterprises = Collections.synchronizedList(enterpriseIds);
        isSynchronizedWithDB = false;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        isSynchronizedWithDB = false;
    }


    public List<Integer> getEnterprises(){
        return enterprises;
    }

    public boolean remove(List<Integer> enterprises){
        this.enterprises.removeAll(enterprises);
        isSynchronizedWithDB = false;
        return this.enterprises.size() <= 0;
    }

    public void add(List<Integer> enterprises){
        Set<Integer> set = new HashSet<>(this.enterprises);
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

    @SuppressWarnings("NullableProblems")
    @Override
    public int compareTo(Tag o) {
        if (o == null) return 1;
        if (this.equals(o)) return 0;
        return title.compareTo(o.title);
    }
}
