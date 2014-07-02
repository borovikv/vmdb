package md.varoinform.model.dao;

import md.varoinform.model.util.SessionManager;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/9/13
 * Time: 12:04 PM
 */
public class TransactionDaoHibernateImpl<T, PK extends Serializable> extends GenericDaoHibernateImpl<T, PK> {
    public TransactionDaoHibernateImpl(Class<T> type) {
        super(type);
    }

    @Override
    public PK save(T instance) {
        try{
            Transaction transaction = getSession().beginTransaction();
            PK pk =  super.save(instance);
            transaction.commit();
            return pk;

        } catch (RuntimeException e){
            getSession().getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void update(T transientObject) {
        Transaction transaction = getSession().beginTransaction();
        super.update(transientObject);
        transaction.commit();
    }

    @Override
    public void delete(T persistentObject) {
        Transaction transaction = getSession().beginTransaction();
        super.delete(persistentObject);
        transaction.commit();
    }

    @Override
    public List<T> getAll() {
        Session session = SessionManager.instance.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            @SuppressWarnings("unchecked") List<T> all = session.createCriteria(type).list();
            if (!transaction.wasCommitted()) transaction.commit();
            return all;
        } catch (RuntimeException e){
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    @Override
    public T read(PK id) {
        Session session = SessionManager.instance.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            @SuppressWarnings("unchecked") T t = (T) session.get(type, id);
            if (!transaction.wasCommitted()) transaction.commit();
            return t;
        } catch (RuntimeException e){
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }
}
