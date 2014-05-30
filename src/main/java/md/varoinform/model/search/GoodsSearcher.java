package md.varoinform.model.search;

import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.util.Normalizer;
import md.varoinform.model.util.SessionManager;
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
    public List<Enterprise> search(String q) {
        String field = "t.title";
        Normalizer normalizer = new Normalizer(field, q, Normalizer.RO);
        String hql = "Select distinct e from Enterprise e join e.goods g2p join g2p.good g join g.titles t where " + normalizer.getField() + " like :pattern";
        Query query = SessionManager.getSession().createQuery(hql).setString("pattern", "%" + normalizer.getString() + "%");
        //noinspection unchecked
        return query.list();
    }

}
