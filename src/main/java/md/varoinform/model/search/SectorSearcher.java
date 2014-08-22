package md.varoinform.model.search;

import md.varoinform.model.util.ClosableSession;
import md.varoinform.model.util.Normalizer;
import org.hibernate.Query;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/24/14
 * Time: 10:21 AM
 */
public class SectorSearcher extends Searcher {
    @Override
    public List<Long> search(String q) {
        try (ClosableSession session = new ClosableSession()) {
            String field = "titles.title";
            Normalizer normalizer = new Normalizer(field, q, Normalizer.RO);
            String hql = "Select distinct e.id from Enterprise e join e.contacts cs join cs.sector s join s.titles titles where " + normalizer.getField() + " like :pattern";
            Query query = session.createQuery(hql).setString("pattern", "%" + normalizer.getString() + "%");
            //noinspection unchecked
            return query.list();
        }
    }

}
