package md.varoinform.model.search;

import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.util.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
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
public class SearchEngine {
    public SearchEngine(Session session) {
        /*FullTextSession fullTextSession = Search.getFullTextSession(session);
        try {
            fullTextSession.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }     */
    }

    public List<Enterprise> search(String q) {
        FullTextSession fullTextSession = Search.getFullTextSession(HibernateSessionFactory.getSession());
        Transaction tx = fullTextSession.beginTransaction();

        // create native Lucene query unsing the query DSL
        // alternatively you can write the Lucene query using the Lucene query parser
        // or the Lucene programmatic API. The Hibernate Search DSL is recommended though
        QueryBuilder qb = fullTextSession.getSearchFactory()
                .buildQueryBuilder().forEntity(Enterprise.class).get();
        org.apache.lucene.search.Query query = qb
                .keyword()
                .onFields("titles.title", "goods.good.titles.title", "goods.good.branch.titles.title", "brands.title",
                        "contacts.postalCode", "contacts.houseNumber", "contacts.officeNumber",
                        "contacts.street.titles.title",  "contacts.sector.titles.title", "contacts.town.titles.title",
                        "contacts.region.titles.title", "contacts.topAdministrativeUnit.titles.title",
                        "contacts.emails.email", "contacts.phones.phone", "contacts.urls.url",
                        "contactPersons.person.titles.title", "contactPersons.phones.phone" )
                .matching(q)
                .createQuery();

        // wrap Lucene query in a org.hibernate.Query
        org.hibernate.Query hibQuery =
                fullTextSession.createFullTextQuery(query, Enterprise.class);

        // execute search
        List result = hibQuery.list();

        tx.commit();
        //session.close();
        return result;
    }
}
