package md.varoinform.model.dao;

import md.varoinform.controller.comparators.EnterpriseComparator;
import md.varoinform.controller.entityproxy.EnterpriseProxy;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.entities.Language;
import md.varoinform.model.entities.Tag;
import md.varoinform.model.util.ClosableSession;
import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.*;

public class EnterpriseDao extends TransactionDaoHibernateImpl<Enterprise, Long> {

    public EnterpriseDao() {
        super(Enterprise.class);
    }

    public static Map<Long, Map<String, Object>> getEnterprisesMap(Language language) {
        return getEnterprisesMap(null, language);
    }

    public static Map<Long, Map<String, Object>> getEnterprisesMap(List<Long> idEnterprises, Language language) {
        Map<Long, Map<String, Object>> enterprisesMap = new LinkedHashMap<>();
        try(ClosableSession session = new ClosableSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Criteria criteria = session.createCriteria(Enterprise.class);
                if (idEnterprises != null) {
                    criteria.add(Restrictions.in("id", idEnterprises));
                }
                //noinspection unchecked
                List<Enterprise> enterprises = criteria.list();
                Collections.sort(enterprises, new EnterpriseComparator(language));
                for (Enterprise enterprise : enterprises) {
                    Long id = enterprise.getId();
                    Map<String, Object> cache = enterpriseAsMap(enterprise, language);
                    enterprisesMap.put(id, cache);
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                transaction.rollback();
            }

        }
        return enterprisesMap;
    }

    private static Map<String, Object> enterpriseAsMap(Enterprise enterprise, Language language) {
        Map<String, Object> map = new HashMap<>();
        EnterpriseProxy proxy = new EnterpriseProxy(enterprise, language);
        for (String field : EnterpriseProxy.getFields()) {
            Object value = proxy.get(field);
            map.put(field, value);
        }
        return map;
    }

    public static Map<String, Object> enterpriseAsMap(Long eid, Language language) {
        try (ClosableSession session = new ClosableSession()){
            @SuppressWarnings("unchecked")
            List<Enterprise> entList = session.createCriteria(Enterprise.class).add(Restrictions.eq("id", eid)).list();
            if (entList.size() > 0) return enterpriseAsMap(entList.get(0), language);
        }
        return new HashMap<>();
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
        try(ClosableSession session = new ClosableSession()) {
            Criteria criteria = session.createCriteria(Enterprise.class).add(Restrictions.in("id", ids));
            //noinspection unchecked
            return criteria.list();
        }
    }

    public List<Long> getEnterpriseIdsByTag(Tag tag) {
        String hql = "Select distinct e.id from Tag t join t.enterprises e where t.id = " + tag.getId();
        try (ClosableSession session = new ClosableSession()){
            //noinspection unchecked
            return session.createQuery(hql).setCacheable(false).list();
        }
    }

}