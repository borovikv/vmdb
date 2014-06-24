package md.varoinform.model.search;

import md.varoinform.model.util.SessionManager;
import org.hibernate.Query;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/24/14
 * Time: 10:20 AM
 */
public class PostalCodeSearcher extends Searcher {
    @Override
    public List<Long> search(String q) {
        String hql = "Select distinct e.id from Enterprise e join e.contacts cs where lower(cs.postalCode) = :pattern";
        Query query = SessionManager.getSession().createQuery(hql).setString("pattern", getCode(q));
        //noinspection unchecked
        return query.list();
    }

    private String getCode(String q) {
        String postalCode = q.trim().toLowerCase();
        return postalCode.replaceAll("md-*", "");
    }

}
