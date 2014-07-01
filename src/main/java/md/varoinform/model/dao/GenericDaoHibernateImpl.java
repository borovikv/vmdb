package md.varoinform.model.dao;

import md.varoinform.model.util.SessionManager;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/9/13
 * Time: 10:28 AM
 */
public class GenericDaoHibernateImpl<T, PK extends Serializable> implements GenericDao<T, PK>{
    private final Class<T> type;

    public GenericDaoHibernateImpl(Class<T> type) {
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    @Override
    public PK save(T newInstance) {
        return (PK)getSession().save(newInstance);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T read(PK id) {
        return (T)getSession().get(type, id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> getAll() {
        return getSession().createCriteria(type).list();
    }

    @Override
    public void update(T transientObject) {
        getSession().update(transientObject);
    }

    @Override
    public void delete(T persistentObject) {
        getSession().delete(persistentObject);
    }

    public Session getSession() {
        return SessionManager.instance.getSession();
    }


}
