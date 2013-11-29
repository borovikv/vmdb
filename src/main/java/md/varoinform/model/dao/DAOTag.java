package md.varoinform.model.dao;

import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.entities.Tag;
import org.hibernate.criterion.Restrictions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/29/13
 * Time: 11:08 AM
 */
public class DAOTag extends TransactionDaoHibernateImpl<Tag, Long>{
    public DAOTag() {
        super(Tag.class);
    }

    public Tag read(String title) {
        @SuppressWarnings("unchecked")
        List<Tag> tags = getSession().createCriteria(Tag.class).add(Restrictions.eq("title", title)).list();
        if (tags.isEmpty()) return null;
        return tags.get(0);
    }

    public void createTag(String title, List<Enterprise> enterprises) {

        Tag tag = read(title);
        if (tag == null){
            tag = new Tag();
            tag.setTitle(title);
            Set<Enterprise> enterpriseSet = new HashSet<>(enterprises);
            tag.setEnterprises(enterpriseSet);
        } else {
            tag.getEnterprises().addAll(enterprises);
        }

        create(tag);
    }

    public boolean removeTag(String title, List<Enterprise> enterprises){
        if (enterprises.isEmpty()) return false;

        Tag tag = read(title);
        if (tag == null) return false;

        tag.removeAll(enterprises);
        if (tag.getEnterprises().isEmpty()){
            delete(tag);
            return true;
        } else {
            create(tag);
            return false;
        }
    }
}
