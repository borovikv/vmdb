package md.varoinform.model.search;

import md.varoinform.model.entities.enterprise.Enterprise;
import md.varoinform.model.utils.DefaultClosableSession;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/25/13
 * Time: 2:44 PM
 */
public class FullTextSearcher extends Searcher {
    private final String[] fields;


    public FullTextSearcher(String[] fields) {
        this.fields = fields;
    }

    public static boolean createIndex() {
        boolean success = false;
        try (DefaultClosableSession session = new DefaultClosableSession()) {
            FullTextSession fullTextSession = Search.getFullTextSession(session.getSession());
            fullTextSession.createIndexer().startAndWait();
            success = true;
        } catch (RuntimeException | InterruptedException e) {
            e.printStackTrace();
        }
        return success;
    }

    @Override
    public List<Integer> search(String q) {
        try(DefaultClosableSession session = new DefaultClosableSession()) {
            FullTextSession fullTextSession = Search.getFullTextSession(session.getSession());
            QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Enterprise.class).get();
            LuceneQueryBuilder builder = new LuceneQueryBuilder(queryBuilder, fields);
            //Transaction tx = fullTextSession.beginTransaction();
            try {
                org.apache.lucene.search.Query query = builder.createQuery(q);
                if (query == null) return new ArrayList<>();
                org.hibernate.search.FullTextQuery hibQuery = fullTextSession.createFullTextQuery(query, Enterprise.class);
                hibQuery.setProjection("id");
                return getIds(hibQuery.list());
            } catch (Exception ex) {
                // tx.rollback();
                ex.printStackTrace();
                return new ArrayList<>();
            }
        }
    }

    public List<Integer> getIds(List list) {
        List<Integer> result = new ArrayList<>();
        for (Object obj : list) {
            Integer id = (Integer) ((Object[])obj)[0];
            result.add(id);
        }
        return result;
    }

    @Override
    public String getName() {
        return "default";
    }

    @Override
    public int compareTo(Searcher o) {
        return -1;
    }
}
