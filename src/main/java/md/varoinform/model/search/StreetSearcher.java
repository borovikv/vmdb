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
 * Time: 10:21 AM
 */
public class StreetSearcher extends Searcher {
    @Override
    public List<Enterprise> search(String q) {
        String field = "titles.title";
        Normalizer normalizer = new Normalizer(field, q, Normalizer.RO);
        String hql = "Select distinct e from Enterprise e join e.contacts cs join cs.street s join s.titles titles where " + normalizer.getField() + " like :pattern";
        Query query = SessionManager.getSession().createQuery(hql).setString("pattern", "%" + normalizer.getString() + "%");
        //noinspection unchecked
        return query.list();
    }

}
