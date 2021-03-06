package md.varoinform.model.search;

import md.varoinform.model.util.ClosableSession;
import md.varoinform.model.util.Normalizer;
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
    public List<Long> search(String q) {
        try (ClosableSession session = new ClosableSession()) {
            String field = "titles.title";
            Normalizer normalizer = new Normalizer(field, q, Normalizer.RO);
            String hql = "select distinct e.id from Enterprise e join e.titles titles where " + normalizer.getField() + " like :title";
            Query query = session.createQuery(hql).setString("title", prefix + normalizer.getString() + "%");
            //noinspection unchecked
            return query.list();
        }
    }

    @Override
    public String getName() {
        if (start) return super.getName();
        return super.getName() + "Contains";
    }
}