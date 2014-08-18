package md.varoinform.model.util;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;


/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 8/11/14
 * Time: 3:12 PM
 */
public class ClosableSession implements AutoCloseable {
    private Session session;

    public ClosableSession() {
        session = SessionManager.instance.getSession();
    }

    public ClosableSession(Configuration cfg){
        if (cfg == null){
            session = SessionManager.instance.getSession();
        } else {
            session = SessionManager.instance.getSession(cfg);
        }
    }

    @Override
    public void close() {
        if (session.isOpen()) {
            session.clear();
            session.close();
        }
    }

    public Transaction beginTransaction() {
        return session.beginTransaction();
    }

    public Criteria createCriteria(Class cls) {
        return session.createCriteria(cls);
    }

    public Transaction getTransaction() {
        return session.getTransaction();
    }

    public Query createQuery(String hql) {
        return session.createQuery(hql);
    }

    public void evict(Object o) {
        session.evict(o);
    }

    public void replicate(Object o, ReplicationMode overwrite) {
        session.replicate(o, overwrite);
    }
}
