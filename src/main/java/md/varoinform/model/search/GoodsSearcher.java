package md.varoinform.model.search;

import md.varoinform.model.utils.DefaultClosableSession;
import md.varoinform.model.utils.Normalizer;
import org.hibernate.Query;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/24/14
 * Time: 10:20 AM
 */
public class GoodsSearcher extends Searcher {
    @Override
    public List<Long> search(String q) {
        try (DefaultClosableSession session = new DefaultClosableSession()) {
            String field = "t.title";
            Normalizer normalizer = new Normalizer(field, q, Normalizer.RO);
            String hql = "Select distinct e.id from Enterprise e join e.goods g2p join g2p.good g join g.titles t where " + normalizer.getField() + " like :pattern";
            Query query = session.createQuery(hql).setString("pattern", "%" + normalizer.getString() + "%");
            //noinspection unchecked
            return query.list();
        }
    }

}
