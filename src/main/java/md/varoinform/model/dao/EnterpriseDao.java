package md.varoinform.model.dao;

import md.varoinform.controller.comparators.EnterpriseComparator;
import md.varoinform.controller.entityproxy.EnterpriseProxy;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.util.ClosableSession;
import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.*;

public class EnterpriseDao  {


    public static Map<Long, Map<String, Object>> getEnterprisesMap(Long langID) {
        return getEnterprisesMap(null, langID);
    }

    public static Map<Long, Map<String, Object>> getEnterprisesMap(List<Long> idEnterprises, Long langID) {
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
                Collections.sort(enterprises, new EnterpriseComparator(langID));
                for (Enterprise enterprise : enterprises) {
                    Long id = enterprise.getId();
                    Map<String, Object> cache = enterpriseAsMap(enterprise, langID);
                    enterprisesMap.put(id, cache);
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                transaction.rollback();
            }

        }
        return enterprisesMap;
    }

    private static Map<String, Object> enterpriseAsMap(Enterprise enterprise, Long langID) {
        Map<String, Object> map = new HashMap<>();
        EnterpriseProxy proxy = new EnterpriseProxy(enterprise, langID);
        for (String field : EnterpriseProxy.getFields()) {
            Object value = proxy.get(field);
            map.put(field, value);
        }
        return map;
    }

    public static Map<String, Object> enterpriseAsMap(Long eid, Long langID) {
        try (ClosableSession session = new ClosableSession()){
            @SuppressWarnings("unchecked")
            List<Enterprise> entList = session.createCriteria(Enterprise.class).add(Restrictions.eq("id", eid)).list();
            if (entList.size() > 0) return enterpriseAsMap(entList.get(0), langID);
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


    public static List<Enterprise> read(ClosableSession session, List<Long> ids) {
        Criteria criteria = session.createCriteria(Enterprise.class).add(Restrictions.in("id", ids));
        //noinspection unchecked
        return criteria.list();
    }

}