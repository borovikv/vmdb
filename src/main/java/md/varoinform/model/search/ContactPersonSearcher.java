package md.varoinform.model.search;

import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.util.SessionManager;
import org.hibernate.Query;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/24/14
 * Time: 10:18 AM
 */
public class ContactPersonSearcher extends Searcher {
    @Override
    public List<Enterprise> search(String q) {
        String hql = "Select distinct e from Enterprise e join e.contactPersons cp join cp.person p join p.titles t where lower(t.title) like :pattern";
        Query query = SessionManager.getSession().createQuery(hql).setString("pattern", "%" + q.toLowerCase() + "%");
        //noinspection unchecked
        return query.list();
    }


}
