package md.varoinform.model.search;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.entities.Language;
import md.varoinform.model.util.SessionManager;
import md.varoinform.util.ResourceBundleHelper;
import org.hibernate.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/24/14
 * Time: 10:19 AM
 */
public class ForeingCapitalSearcher extends Searcher {
    @Override
    public List<Long> search(String q) {
        String hql = "Select distinct e.id from Enterprise e where e.foreignCapital = :hasForeingCapital";
        try{
            boolean has = convertToBoolean(q);
            Query query = SessionManager.instance.getSession().createQuery(hql).setBoolean("hasForeingCapital", has);
            //noinspection unchecked
            return query.list();
        } catch (IllegalArgumentException e){
            return new ArrayList<>();
        }
    }

    private Boolean convertToBoolean(String q) {
        List<String> list = new ArrayList<>();
        List<String> all = new ArrayList<>();
        List<Language> languages = LanguageProxy.instance.getLanguages();
        for (Language language : languages) {
            String falseStr = ResourceBundleHelper.getString(language, "false", "no").toLowerCase();
            String trueStr = ResourceBundleHelper.getString(language, "true", "yes").toLowerCase();
            list.add(trueStr);
            all.add(falseStr);
            all.add(trueStr);
        }
        String value = q.trim().toLowerCase();
        if (!all.contains(value)) throw new IllegalArgumentException();
        return list.contains(value);
    }

}
