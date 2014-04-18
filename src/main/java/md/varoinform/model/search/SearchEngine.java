package md.varoinform.model.search;

import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.util.SessionManager;
import org.apache.lucene.search.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.PhraseMatchingContext;
import org.hibernate.search.query.dsl.QueryBuilder;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/25/13
 * Time: 2:44 PM
 */
@SuppressWarnings("UnusedDeclaration")
public class SearchEngine {

    public void createIndex(Session session) {
        FullTextSession fullTextSession = Search.getFullTextSession(session);
        try {
            fullTextSession.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Enterprise> search(String q, String field){
        if (field == null) return search(q);
        return null;
    }

    private List<Enterprise> search(String q) {
        FullTextSession fullTextSession = Search.getFullTextSession(SessionManager.getSession());
        Transaction tx = fullTextSession.beginTransaction();
        try {
            org.hibernate.Query hibQuery = getQuery(fullTextSession, q);
            @SuppressWarnings("unchecked")
            List<Enterprise> result = (List<Enterprise>)hibQuery.list();
            tx.commit();
            return result;
        } catch (Exception ex) {
            tx.rollback();
            throw ex;
        }
    }

    private org.hibernate.Query getQuery(FullTextSession fullTextSession, String q) {
        org.apache.lucene.search.Query query = getLuceneQuery(fullTextSession, q);

        // wrap Lucene query in a org.hibernate.Query
        return fullTextSession.createFullTextQuery(query, Enterprise.class);
    }

    private Query getLuceneQuery(FullTextSession fullTextSession, String q) {
        // save native Lucene query unsing the query DSL
        // alternatively you can write the Lucene query using the Lucene query parser
        // or the Lucene programmatic API. The Hibernate Search DSL is recommended though
        QueryBuilder qb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Enterprise.class).get();
        /*return qb.keyword().fuzzy().withThreshold(0.8f).withPrefixLength(1)
                .onFields("titles.title", "goods.good.titles.title", "goods.good.treeNodes.title.titles.title", "brands.title",
                        "contacts.postalCode", "contacts.houseNumber", "contacts.officeNumber",
                        "contacts.street.titles.title", "contacts.sector.titles.title", "contacts.town.titles.title",
                        "contacts.region.titles.title",
                        "contacts.emails.email", "contacts.phones.phone", "contacts.urls.url",
                        "contactPersons.person.titles.title", "contactPersons.phones.phone")
                .matching(q)
                .createQuery();
        */
        String[] strings = {"goods.good.titles.title", "goods.good.treeNodes.title.titles.title", "brands.title",
                "contacts.postalCode", "contacts.houseNumber", "contacts.officeNumber",
                "contacts.street.titles.title", "contacts.sector.titles.title", "contacts.town.titles.title",
                "contacts.region.titles.title",
                "contacts.emails.email", "contacts.phones.phone", "contacts.urls.url",
                "contactPersons.person.titles.title", "contactPersons.phones.phone"};
        PhraseMatchingContext phraseMatchingContext = qb.phrase().onField("titles.title");
        for (String string : strings) {
            phraseMatchingContext.andField(string);
        }
        return phraseMatchingContext.sentence(q).createQuery();

    }
}
