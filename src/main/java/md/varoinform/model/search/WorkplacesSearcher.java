package md.varoinform.model.search;

import md.varoinform.model.util.ClosableSession;
import org.hibernate.Query;

import java.util.ArrayList;
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
    public List<Long> search(String q) {
        try (ClosableSession session = new ClosableSession()) {
            Integer amount = Integer.parseInt(q.trim());
            String hql = "Select distinct e.id from Enterprise e where e.workplaces " + type + " :amount";
            Query query = session.createQuery(hql).setInteger("amount", amount);
            //noinspection unchecked
            return query.list();
        } catch (NumberFormatException e){
            return new ArrayList<>();
        }
    }

    @Override
    public String getName() {
        if (type == ComparisonType.EQ) return super.getName();
        return super.getName() + type.toString();
    }
}
