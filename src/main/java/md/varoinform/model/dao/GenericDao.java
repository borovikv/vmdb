package md.varoinform.model.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/9/13
 * Time: 10:23 AM
 */
public interface GenericDao <T, PK extends Serializable> {
    PK save(T newInstance);
    T read(PK id);
    List<T> getAll();
    void update(T transientObject);
    void delete(T persistentObject);
}
