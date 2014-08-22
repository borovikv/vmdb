package md.varoinform.model.search;

import md.varoinform.model.util.ClosableSession;
import org.hibernate.Query;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/24/14
 * Time: 10:20 AM
 */
public class PhoneSearcher extends Searcher {
    @Override
    public List<Long> search(String q) {
        try (ClosableSession session = new ClosableSession()) {
            String hql = "Select distinct e.id from Enterprise e join e.contacts c join c.allPhones p where lower(p.phone) like :pattern";
            Query query = session.createQuery(hql).setString("pattern", "%" + q.trim().toLowerCase());
            //noinspection unchecked
            return query.list();
        }
    }

}
