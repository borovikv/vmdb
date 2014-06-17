package md.varoinform.model.search;

import md.varoinform.model.util.SessionManager;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.hibernate.search.*;
import org.hibernate.search.query.dsl.*;

import java.util.*;
import java.util.regex.*;

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
    @SuppressWarnings("UnusedDeclaration")
    private QueryType type;

    public LuceneQueryBuilder(QueryBuilder queryBuilder, String[] fields, QueryType type) {
        this.queryBuilder = queryBuilder;
        this.fields = fields;
        this.type = type;
    }

    public Query createQuery(String q) throws ParseException {
        return createStrictQuery(q);
    }

    public Query createStrictQuery(String searchString) throws ParseException {
        BooleanJunction<BooleanJunction> bool = queryBuilder.bool();
        TermMatchingContext matchingContext = getTermMatchingContext();
        List<String> tokenized = getTokenized(searchString);
        for (String s : tokenized) {
            if (s == null || s.isEmpty()) continue;
            try {
                Query query = matchingContext.matching(s).createQuery();
                bool.must(query);
            } catch (Exception ignored) {
            }
        }
        return bool.createQuery();
    }

    public List<String> getTokenized(String searchString) {
        FullTextSession fullTextSession = Search.getFullTextSession(SessionManager.getSession());
        Analyzer analyzer = fullTextSession.getSearchFactory().getAnalyzer("customanalyzer");
        QueryParser parser = new QueryParser(Version.LUCENE_35, "title", analyzer);
        List<String> tokenized = new ArrayList<>();

        try {
            Query query = parser.parse(searchString);
            String cleanedText = query.toString("title");
            Pattern pattern = Pattern.compile("\\({0,1}(\\w+\\s)*(\\w+)\\){0,1}", Pattern.UNICODE_CHARACTER_CLASS);
            Matcher matcher = pattern.matcher(cleanedText);
            while (matcher.find()) {
                String word = matcher.group(2);
                tokenized.add(word.trim());
            }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return tokenized;
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

    public enum QueryType {
        Strict
    }
}
