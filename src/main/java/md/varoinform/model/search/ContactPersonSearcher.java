package md.varoinform.model.search;

import md.varoinform.model.util.Normalizer;
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
    public List<Long> search(String q) {
        String field = "t.title";
        Normalizer normalizer = new Normalizer(field, q, Normalizer.RO);
        String hql = "Select distinct e.id from Enterprise e join e.contactPersons cp join cp.person p join p.titles t where "
                + normalizer.getField() + " like :pattern";
        Query query = SessionManager.instance.getSession().createQuery(hql).setString("pattern", "%" + normalizer.getString() + "%");
        //noinspection unchecked
        return query.list();
    }


}
