package md.varoinform.model.search;

import md.varoinform.model.utils.DefaultClosableSession;
import md.varoinform.model.utils.Normalizer;
import org.hibernate.Query;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/24/14
 * Time: 10:22 AM
 */
public class TownSearcher extends Searcher {
    @Override
    public List<Long> search(String q) {
        try (DefaultClosableSession session = new DefaultClosableSession()) {
            String field = "titles.title";
            Normalizer normalizer = new Normalizer(field, q, Normalizer.RO);
            String hql = "Select distinct e.id " +
                    "from Enterprise e " +
                    "join e.contacts cs " +
                    "join cs.town t " +
                    "join t.titles titles " +
                    "where " + normalizer.getField() + " like :pattern";
            Query query = session.createQuery(hql).setString("pattern", "%" + normalizer.getString() + "%");
            //noinspection unchecked
            return query.list();
        }
    }
}
