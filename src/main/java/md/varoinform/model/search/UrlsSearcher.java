package md.varoinform.model.search;

import md.varoinform.model.utils.DefaultClosableSession;
import org.hibernate.Query;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/24/14
 * Time: 10:22 AM
 */
public class UrlsSearcher extends Searcher {
    @Override
    public List<Integer> search(String q) {
        try (DefaultClosableSession session = new DefaultClosableSession()) {
            String hql = "Select distinct e.id from Enterprise e join e.contacts cs join cs.urls urls where lower(urls.url) like :pattern";
            Query query = session.createQuery(hql).setString("pattern", "%" + q.trim().toLowerCase() + "%");
            //noinspection unchecked
            return query.list();
        }
    }
}
