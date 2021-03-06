package md.varoinform.model.search;

import md.varoinform.model.util.ClosableSession;
import org.hibernate.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/24/14
 * Time: 10:19 AM
 */
public class CreationDateSearcher extends Searcher {
    @Override
    public List<Long> search(String q) {
        try (ClosableSession session = new ClosableSession()) {
            String hql = "Select distinct e.id from Enterprise e where e.creation = :creation";
            if (!Pattern.matches("^[0-9]+$", q.trim())) return new ArrayList<>();
            int year = Integer.parseInt(q.trim());
            Query query = session.createQuery(hql).setInteger("creation", year);
            //noinspection unchecked
            return query.list();
        }
    }
}
