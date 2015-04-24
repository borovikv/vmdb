package md.varoinform.model.dao;

import md.varoinform.model.entities.Database;
import md.varoinform.model.utils.DefaultClosableSession;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Projections;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 7/1/14
 * Time: 2:10 PM
 */
public class DatabaseDao {

    public static String getUID() {
        try (DefaultClosableSession session = new DefaultClosableSession()) {
            try {
                Transaction transaction = session.beginTransaction();
                @SuppressWarnings("unchecked")
                List<String> uids = session.createCriteria(Database.class).setProjection(Projections.property("uid")).list();
                transaction.commit();
                if (!uids.isEmpty()) {
                    return uids.get(0);
                }
            } catch (RuntimeException e){
                e.printStackTrace();
                session.getTransaction().rollback();
            }
        }
        return null;
    }

    public static void setUID(String uid){
          setUID(uid, null);
    }

    public static void setUID(String uid, Configuration cfg) {
        Database record = new Database();
        record.setUid(uid);
        try (DefaultClosableSession session = new DefaultClosableSession(cfg)){
            try {
                Transaction transaction = session.beginTransaction();
                session.save(record);
                transaction.commit();

            } catch (RuntimeException re){
                re.printStackTrace();
                session.getTransaction().rollback();
                throw re;
            }
        }
    }

    public static void clear() {
        try (DefaultClosableSession session = new DefaultClosableSession()) {
            try {
                Transaction transaction = session.beginTransaction();
                @SuppressWarnings("unchecked")
                List<Database> all = session.createCriteria(Database.class).list();
                for (Database database : all) {
                    session.delete(database);
                }
                transaction.commit();

            } catch (RuntimeException e){
                e.printStackTrace();
                session.getTransaction().rollback();
            }
        }
    }
}
