package md.varoinform.model.dao;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.controller.entityproxy.EnterpriseProxy;
import md.varoinform.model.entities.enterprise.*;
import md.varoinform.model.utils.DefaultClosableSession;
import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.*;

public class EnterpriseDao  {


    public static List<Integer> getEIDs() {
        return getEIDs(null, LanguageProxy.instance.getCurrentLanguage());
    }
    public static List<Integer> getEIDs(Criterion criterion, Integer langID) {
        try(DefaultClosableSession session = new DefaultClosableSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Criteria criteria = session.createCriteria(EnterpriseTitle.class)
                        .addOrder(Order.asc("title"))
                        .setProjection(Projections.property("enterprise.id"));
                if (criterion != null){
                    criteria.add(criterion);
                }
                @SuppressWarnings("unchecked")
                List<Integer> list = criteria.list();
                transaction.commit();
                return list;

            } catch (RuntimeException e) {
                e.printStackTrace();
                transaction.rollback();
            }
        }
        return new ArrayList<>();
    }


    public static Map<Integer, Map<String, Object>> getEnterprisesMap(Integer langID) {
        return getEnterprisesMap((List<Integer>)null, langID);
    }

    public static Map<Integer, Map<String, Object>> getEnterprisesMap(List<Integer> idEnterprises, Integer langID) {
        Map<Integer, Map<String, Object>> enterprisesMap = new LinkedHashMap<>();
        try(DefaultClosableSession session = new DefaultClosableSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Criteria criteria = session.createCriteria(Enterprise.class);
                if (idEnterprises != null) {
                    criteria.add(Restrictions.in("id", idEnterprises));
                }
                //noinspection unchecked
                List<Enterprise> enterprises = criteria.list();
                for (Enterprise enterprise : enterprises) {
                    Integer id = enterprise.getId();
                    Map<String, Object> cache = enterpriseAsMap(enterprise, langID);
                    enterprisesMap.put(id, cache);
                }
                transaction.commit();
            } catch (RuntimeException e) {
                e.printStackTrace();
                transaction.rollback();
            }

        } catch(RuntimeException e) {
            e.printStackTrace();

        }
        System.out.println(enterprisesMap);
        return enterprisesMap;
    }

    private static Map<String, Object> enterpriseAsMap(Enterprise enterprise, Integer langID) {
        Map<String, Object> map = new HashMap<>();
        EnterpriseProxy proxy = new EnterpriseProxy(enterprise, langID);
        for (String field : EnterpriseProxy.getFields()) {
            Object value = proxy.get(field);
            map.put(field, value);
        }
        return map;
    }

    public static Map<String, Object> enterpriseAsMap(Integer eid, Integer langID) {
        try (DefaultClosableSession session = new DefaultClosableSession()){
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
        try (DefaultClosableSession session = new DefaultClosableSession(cfg)) {
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

    public static Integer countWhereLastChangeGTE(Configuration cfg, Date date) {
        if (date == null) return 0;

        try (DefaultClosableSession session = new DefaultClosableSession(cfg)) {
            try {
                Transaction transaction = session.beginTransaction();
                Criteria criteria = session
                        .createCriteria(Enterprise.class)
                        .add(Restrictions.gt("lastChange", date))
                        .setProjection(Projections.rowCount());
                Integer result = (Integer) criteria.uniqueResult();
                transaction.commit();
                return result;

            } catch (RuntimeException ignored) {
                session.getTransaction().rollback();
            }
        }

        return 0;
    }


    public static List<Enterprise> read(DefaultClosableSession session, List<Integer> ids) {
        Criteria criteria = session.createCriteria(Enterprise.class).add(Restrictions.in("id", ids));
        //noinspection unchecked
        return criteria.list();
    }

    public static  Map<String, Object> getEnterprisesMap(Integer eid, Integer langID) {
        return getEnterprisesMap(Collections.singletonList(eid), langID).get(eid);
    }
}