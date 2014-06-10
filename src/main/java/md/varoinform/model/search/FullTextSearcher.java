package md.varoinform.model.search;

import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.util.SessionManager;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.hibernate.CacheMode;
import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.TermMatchingContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/25/13
 * Time: 2:44 PM
 */
public class FullTextSearcher extends Searcher {

    private final List<String> stopWords;
    private static final QueryBuilder queryBuilder;
    private static final FullTextSession fullTextSession;
    static {
        fullTextSession = Search.getFullTextSession(SessionManager.getSession());
        queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Enterprise.class).get();
    }

    public FullTextSearcher() {
        //createIndex(SessionManager.getSession());
        stopWords = getStopWords();
    }

    @SuppressWarnings("UnusedDeclaration")
    public void createIndex(Session session) {
        try {
            fullTextSession
                    .createIndexer()
                    .batchSizeToLoadObjects( 25 )
                    .cacheMode( CacheMode.NORMAL )
                    .threadsToLoadObjects( 5 )
                    .idFetchSize( 150 )
                    .threadsForSubsequentFetching(20)
                    //.progressMonitor( monitor ) //a MassIndexerProgressMonitor implementation
                    .startAndWait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Enterprise> search(String q) {
        //Transaction tx = fullTextSession.beginTransaction();
        try {
            org.apache.lucene.search.Query query = getLuceneQuery(q);
            org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery(query, Enterprise.class);
            @SuppressWarnings("unchecked")
            List<Enterprise> result = (List<Enterprise>)hibQuery.list();
            //System.out.println(result);
            //tx.commit();
            return result;
        } catch (Exception ex) {
           // tx.rollback();
            ex.printStackTrace();
            return null;
        }
    }

    private Query getLuceneQuery(String q) throws ParseException {

        BooleanJunction<BooleanJunction> bool = queryBuilder.bool();
        TermMatchingContext all = queryBuilder.keyword()
                .onFields("titles.title", "goods.good.titles.title","brands.title",
                        "contacts.postalCode", "contacts.houseNumber", "contacts.officeNumber",
                        "contacts.street.titles.title", "contacts.sector.titles.title", "contacts.town.titles.title",
                        "contacts.region.titles.title",
                        "contacts.region.id",
                        "contacts.emails.email", "contacts.phones.phone", "contacts.urls.url",
                        "contactPersons.person.titles.title", "contactPersons.phones.phone");
        String[] split = q.split("\\s+");
        for (String s : split) {
            if (s == null || s.isEmpty() || stopWords.contains(s)) {
                continue;
            }
            bool.must(all.matching(s).createQuery());
        }
        return bool.createQuery();

    }

    private List<String> getStopWords(){
        List<String> stopWords = new ArrayList<>();
        InputStream resource = getClass().getResourceAsStream("/word.txt");
        if (resource == null) return stopWords;
        try(InputStreamReader inputStreamReader = new InputStreamReader(resource); BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line;
            while ((line = reader.readLine()) != null){
                stopWords.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stopWords;
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
