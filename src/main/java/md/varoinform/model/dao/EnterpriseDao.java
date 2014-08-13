package md.varoinform.model.dao;

import md.varoinform.controller.Cache;
import md.varoinform.controller.comparators.EnterpriseComparator;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.entities.Tag;
import md.varoinform.model.util.ClosableSession;
import md.varoinform.model.util.SessionManager;
import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class EnterpriseDao extends TransactionDaoHibernateImpl<Enterprise, Long> {
    public EnterpriseDao() {
        super(Enterprise.class);
    }

    public static List<Enterprise> getEnterprises() {
        //noinspection unchecked
        List<Enterprise> enterprises = SessionManager.instance.getSession().createCriteria(Enterprise.class).list();
        Collections.sort(enterprises, new EnterpriseComparator());
        return enterprises;
    }

    public static Date getMaxCheckDate(){
        return getMaxCheckDate(null);
    }

    public static Date getMaxCheckDate(Configuration cfg) {
        try (ClosableSession session = new ClosableSession(cfg)) {
            Transaction transaction = session.beginTransaction();
            try {
                Criteria criteria = session
                        .createCriteria(Enterprise.class)
                        .setProjection(Projections.max("lastChange"));
                Date date = (Date) criteria.uniqueResult();
                transaction.commit();
                return date;
            } catch (RuntimeException ignored) {
                transaction.rollback();
            }
        }

        return null;
    }

    public static Long countWhereLastChangeGTE(Configuration cfg, Date date) {
        if (date == null) return 0L;

        try (ClosableSession session = new ClosableSession(cfg)) {
            try {
                Transaction transaction = session.beginTransaction();
                Criteria criteria = session
                        .createCriteria(Enterprise.class)
                        .add(Restrictions.gt("lastChange", date))
                        .setProjection(Projections.rowCount());
                Long result = (Long) criteria.uniqueResult();
                transaction.commit();
                return result;

            } catch (RuntimeException ignored) {
                session.getTransaction().rollback();
            }
        }

        return 0L;
    }


    public List<Enterprise> read(List<Long> ids) {
        if (Cache.instance.isEnterpriseCached()) {
            return Cache.instance.getEnterprises(ids);
        } else {
            Criteria criteria = SessionManager.instance.getSession().createCriteria(Enterprise.class).add(Restrictions.in("id", ids));
            //noinspection unchecked
            return criteria.list();
        }
    }

    public List<Long> getEnterpriseIdsByTag(Tag tag) {
        ArrayList<Long> ids = new ArrayList<>();
        for (Enterprise enterprise : tag.getEnterprises()) {
            ids.add(enterprise.getId());
        }
        return ids;
    }
}