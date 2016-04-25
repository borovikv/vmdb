package md.varoinform.model.dao;

import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.utils.DefaultClosableSession;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class EnterpriseDao  {

    public static Enterprise getEnterprise(Integer id) {
        try(DefaultClosableSession session = new DefaultClosableSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                return (Enterprise) session.getSession().get(Enterprise.class, id);
            } catch (RuntimeException e) {
                e.printStackTrace();
                transaction.rollback();
                throw new NoSuchElementException();
            }
        }
    }

    public static List<Integer> getAllEnterpriseIds() {
        try (DefaultClosableSession session = new DefaultClosableSession()){
            return (List<Integer>) session.getSession().createCriteria(Enterprise.class).addOrder(Order.asc("title"))
                    .setProjection(Projections.property("id")).list();
        }
    }

    public static Map<String, Object> enterprisesAsMap(Integer eid, Integer langID) {
        return null;
    }
}