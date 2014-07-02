package md.varoinform.model.dao;

import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.entities.Tag;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
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

    public Tag createTag(String title, List<Enterprise> enterprises) {
        if (title == null || title.isEmpty()) return null;

        Tag tag = read(title);
        if (tag == null){
            tag = new Tag();
            tag.setTitle(title);
            Set<Enterprise> enterpriseSet = new HashSet<>(enterprises);
            tag.setEnterprises(enterpriseSet);
        } else {
            tag.getEnterprises().addAll(enterprises);
        }
        return tag;
    }


    public void delete(Set<Tag> tagsToDelete) {
        if (tagsToDelete.isEmpty()) return;
        try{
            Transaction transaction = getSession().beginTransaction();
            for (Tag tag : tagsToDelete) {
                getSession().delete(tag);
            }
            transaction.commit();
        } catch (RuntimeException e){
            e.printStackTrace();
            getSession().getTransaction().rollback();
        }
    }

    public void save(Set<Tag> tagsToSave) {
        if (tagsToSave.isEmpty()) return;
        Session session = getSession();
        try{
            Transaction transaction = session.beginTransaction();
            for (Tag tag : tagsToSave) {
                if (tag.getId() != null){
                    session.replicate(tag, ReplicationMode.OVERWRITE);
                } else {
                    session.save(tag);
                }
            }
            transaction.commit();
        } catch (RuntimeException e){
            e.printStackTrace();
            session.getTransaction().rollback();
        }
    }
}
