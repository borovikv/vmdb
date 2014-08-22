package md.varoinform.model.dao;

import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.entities.Tag;
import md.varoinform.model.util.ClosableSession;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/29/13
 * Time: 11:08 AM
 */
public class DAOTag {
    public List<md.varoinform.controller.cache.Tag> getAll() {
        List<md.varoinform.controller.cache.Tag> result = new ArrayList<>();
        try (ClosableSession session = new ClosableSession()) {
            try {
                Transaction transaction = session.beginTransaction();
                @SuppressWarnings("unchecked")
                List<Tag> tags = session.createCriteria(Tag.class).list();
                for (Tag tag : tags) {
                    result.add(new md.varoinform.controller.cache.Tag(tag));
                }
                transaction.commit();
            } catch (RuntimeException e) {
                e.printStackTrace();
                session.getTransaction().rollback();
            }
        }
        return result;
    }

    public void delete(md.varoinform.controller.cache.Tag tag) {
        if (tag == null || tag.getId() < 0) return;

        try (ClosableSession session = new ClosableSession()) {
            try {
                Transaction transaction = session.beginTransaction();
                @SuppressWarnings("unchecked")
                List<Tag> tags = session.createCriteria(Tag.class).add(Restrictions.eq("id", tag.getId())).list();
                for (Tag t : tags) {
                    session.delete(t);
                }
                transaction.commit();
            } catch (RuntimeException e) {
                e.printStackTrace();
                session.getTransaction().rollback();
            }
        }
    }

    public static void synchronizeWithDB(Collection<md.varoinform.controller.cache.Tag> tags){
        synchronizeWithDB(tags, null, true);
    }

    public static void synchronizeWithDB(Collection<md.varoinform.controller.cache.Tag> tags, Configuration cfg, boolean checkSynchronization) {
        if (tags.isEmpty()) return;
        try (ClosableSession session = new ClosableSession(cfg)) {
            try {
                Transaction transaction = session.beginTransaction();
                for (md.varoinform.controller.cache.Tag tag : tags) {
                    if (!checkSynchronization || !tag.isSynchronizedWithDB()) {
                        Tag t = getOrCreateTag(tag, session);
                        session.save(t);
                        tag.setSynchronizedWithDB(true);
                        tag.setId(t.getId());
                    }
                }
                transaction.commit();
            } catch (RuntimeException e) {
                e.printStackTrace();
                session.getTransaction().rollback();
            }
        }
    }

    private static Tag getOrCreateTag(md.varoinform.controller.cache.Tag t, ClosableSession session) {
        if (t.getId() != null && t.getId() > 0) {
            @SuppressWarnings("unchecked")
            List<Tag> tags = session.createCriteria(Tag.class).add(Restrictions.eq("id", t.getId())).list();
            if (!tags.isEmpty()) {
                Tag tag = tags.get(0);
                tag.setTitle(t.getTitle());
                tag.setEnterprises(new HashSet<>(new EnterpriseDao().read(session, t.getEnterprises())));
                return tag;
            }
        }
        return createTag(t, session);
    }

    private static Tag createTag(md.varoinform.controller.cache.Tag t, ClosableSession session) {
        Tag tag = new Tag();
        tag.setTitle(t.getTitle());
        Set<Enterprise> enterpriseSet = new HashSet<>(new EnterpriseDao().read(session, t.getEnterprises()));
        tag.setEnterprises(enterpriseSet);
        return tag;
    }

}
