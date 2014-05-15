package md.varoinform.model.search;

import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.util.SessionManager;
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
    public List<Enterprise> search(String q) {
        String hql = "Select distinct e from Enterprise e where e.creation = :creation";
        if (!Pattern.matches("[0-9]+", q))return new ArrayList<>();
        int year = Integer.parseInt(q);
        Query query = SessionManager.getSession().createQuery(hql).setInteger("creation", year);
        //noinspection unchecked
        return query.list();
    }
}
