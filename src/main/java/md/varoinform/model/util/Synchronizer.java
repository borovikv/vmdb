package md.varoinform.model.util;

import org.hibernate.HibernateException;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 3/28/14
 * Time: 11:21 AM
 */
public class Synchronizer {
    public static void synchronize(Class hibernateClass, Session from, Session to) throws HibernateException
    {
        Transaction trans = to.beginTransaction();
        List newData = from.createCriteria(hibernateClass).list();
        for (Object o : newData) {
            from.evict(o);
            to.replicate(o, ReplicationMode.OVERWRITE);
        }
        trans.commit();

    }
}
