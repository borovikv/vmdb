package md.varoinform.model.search;

import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.util.SessionManager;
import org.hibernate.Query;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/24/14
 * Time: 10:18 AM
 */
public class BranchSearcher extends Searcher {
    public static void main(String[] args) {
        System.out.println(new BranchSearcher().search("авто").size());
    }
    @Override
    public List<Enterprise> search(String q) {
        String hql = "Select distinct " +
                "e from Enterprise e " +
                "join e.goods good " +
                "join good.good.treeNodes tn " +
                "join tn.title title " +
                "join title.titles titles " +
                "where lower(titles.title) like :title";
        Query query = SessionManager.getSession().createQuery(hql).setString("title", "%" + q.toLowerCase() + "%");
        //noinspection unchecked
        return query.list();
    }

}
