package md.varoinform.model.search;

import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.util.SessionManager;
import org.hibernate.Query;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/24/14
 * Time: 10:21 AM
 */
public class BrandsSearcher extends Searcher {
    @Override
    public List<Enterprise> search(String q) {
        String hql = "Select distinct e from Enterprise e join e.brands bs where lower(bs.title) like :pattern";
        Query query = SessionManager.getSession().createQuery(hql).setString("pattern", "%" + q.trim().toLowerCase() + "%");
        //noinspection unchecked
        return query.list();
    }

}
