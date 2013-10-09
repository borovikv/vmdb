package md.varoinform.model.dao;

import md.varoinform.model.util.HibernateSessionFactory;
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
    private Class<T> type;

    public GenericDaoHibernateImpl(Class<T> type) {
        this.type = type;
    }

    @Override
    public PK create(T newInstance) {
        return (PK)getSession().save(newInstance);
    }

    @Override
    public T read(PK id) {
        return (T)getSession().get(type, id);
    }

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
        return HibernateSessionFactory.getSession();
    }


}
