package md.varoinform.model.search;

import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.util.SessionManager;
import org.hibernate.Query;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/24/14
 * Time: 10:19 AM
 */
public class ForeingCapitalSearcher extends Searcher {
    @Override
    public List<Enterprise> search(String q) {
        String hql = "Select distinct e from Enterprise e where e.foreignCapital = :hasForeingCapital";
        boolean has = convertToBoolean(q);
        Query query = SessionManager.getSession().createQuery(hql).setBoolean("hasForeingCapital", has);
        //noinspection unchecked
        return query.list();
    }

    private boolean convertToBoolean(String q) {
        return Arrays.asList("yes", "да", "da").contains(q.toLowerCase());
    }

}
