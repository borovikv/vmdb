package md.varoinform.model.search;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.TermContext;
import org.hibernate.search.query.dsl.TermMatchingContext;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 6/12/14
 * Time: 9:13 AM
 */
public class LuceneQueryBuilder {
    public static final String[] ALL_FIELDS = new String[]{"titles.title",
            "goods.good.titles.title",
            //"goods.good.nodes.titles.title",
            "brands.title",
            "contacts.postalCode", "contacts.houseNumber", "contacts.officeNumber",
            "contacts.street.titles.title", "contacts.sector.titles.title", "contacts.town.titles.title",
            "contacts.region.titles.title",
            "contacts.region.id",
            "contacts.emails.email", "contacts.phones.phone", "contacts.urls.url",
            "contactPersons.person.titles.title", "contactPersons.phones.phone"};
    private final QueryBuilder queryBuilder;
    private final String[] fields;


    public LuceneQueryBuilder(QueryBuilder queryBuilder, String[] fields) {
        this.queryBuilder = queryBuilder;
        this.fields = fields;
    }

    public Query createQuery(String q) throws ParseException {
        return createStrictQuery(q);
    }

    public Query createStrictQuery(String searchString) throws ParseException {
        QueryAnalyzer analyzer = new QueryAnalyzer(searchString);
        List<String> tokens = analyzer.getTokens();
        if (tokens.isEmpty()) return null;

        BooleanJunction<BooleanJunction> bool = queryBuilder.bool();
        TermMatchingContext matchingContext = getTermMatchingContext();
        for (String token : tokens) {
            if (token == null || token.isEmpty()) continue;
            try {
                Query query = matchingContext.matching(token).createQuery();
                bool.must(query);
            } catch (Exception ignored) {
            }
        }
        return bool.createQuery();
    }



    public TermMatchingContext getTermMatchingContext() {
        TermContext context = queryBuilder.keyword();
        TermMatchingContext matchingContext = null;
        for (String field : fields) {
            if (matchingContext == null) {
                matchingContext = context.onField(field);
            } else {
                matchingContext.andField(field);
            }
            matchingContext.ignoreAnalyzer();
        }
        return matchingContext;
    }

}
