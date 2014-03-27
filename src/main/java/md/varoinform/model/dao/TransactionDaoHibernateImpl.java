package md.varoinform.model.dao;

import org.hibernate.Transaction;

import java.io.Serializable;

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
    public PK create(T newInstance) {
        try{
            Transaction transaction = getSession().beginTransaction();
            PK pk =  super.create(newInstance);
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
}
