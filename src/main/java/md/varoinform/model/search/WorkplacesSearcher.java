package md.varoinform.model.search;

import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.util.SessionManager;
import org.hibernate.Query;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/24/14
 * Time: 10:22 AM
 */
public class WorkplacesSearcher extends Searcher {
    private final ComparisonType type;

    public static enum ComparisonType {
        GTE(">"), LTE("<"), EQ("=");
        private final String operator;

        ComparisonType(String operator) {
            this.operator = operator;
        }

        @Override
        public String toString() {
            return operator;
        }
    }

    public WorkplacesSearcher(ComparisonType type ) {
        this.type = type;
    }

    @Override
    public List<Enterprise> search(String q) {
        Integer amount = Integer.parseInt(q);
        String hql = "Select distinct e from Enterprise e where e.workplaces " + type + " :amount";
        Query query = SessionManager.getSession().createQuery(hql).setInteger("amount", amount);
        //noinspection unchecked
        return query.list();
    }

    @Override
    public String getName() {
        return super.getName() + type.toString();
    }
}
