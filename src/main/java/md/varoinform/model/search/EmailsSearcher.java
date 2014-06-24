package md.varoinform.model.search;

import md.varoinform.model.util.SessionManager;
import org.hibernate.Query;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/24/14
 * Time: 10:19 AM
 */
public class EmailsSearcher extends Searcher {
    @Override
    public List<Long> search(String q) {
        String hql = "Select distinct e.id from Enterprise e join e.contacts cs join cs.emails email where lower(email.email) = :pattern";
        Query query = SessionManager.getSession().createQuery(hql).setString("pattern", q.trim().toLowerCase());
        //noinspection unchecked
        return query.list();
    }

}
