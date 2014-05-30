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
 * Time: 10:22 AM
 */
public class TitleSearcher extends Searcher {
    private final String prefix;
    private final boolean start;

    public TitleSearcher(boolean start) {
        this.start = start;
        if (start) {
            prefix = "";
        } else {
            prefix = "%";
        }
    }

    @Override
    public List<Enterprise> search(String q) {
        String field = "titles.title";
        Normalizer normalizer = new Normalizer(field, q, Normalizer.RO);
        String hql = "select distinct e from Enterprise e join e.titles titles where " + normalizer.getField() + ") like :title";
        Query query = SessionManager.getSession().createQuery(hql).setString("title", prefix + normalizer.getString() + "%");
        //noinspection unchecked
        return query.list();
    }

    @Override
    public String getName() {
        if (start) return super.getName();
        return super.getName() + "Contains";
    }
}