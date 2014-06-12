package md.varoinform.model.search;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.TermMatchingContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

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


    public enum QueryType {
        Strict
    }

    private final QueryBuilder queryBuilder;
    private final String[] fields;
    private QueryType type;

    public LuceneQueryBuilder(QueryBuilder queryBuilder, String[] fields, QueryType type) {
        this.queryBuilder = queryBuilder;
        this.fields = fields;
        this.type = type;
    }

    public Query createQuery(String q) throws ParseException {
        switch (type){
            case Strict:
                return createStrictQuery(q);
        }
        return null;
    }

    public Query createStrictQuery(String q) throws ParseException {
        List<String> stopWords = getStopWords();
        BooleanJunction<BooleanJunction> bool = queryBuilder.bool();
        TermMatchingContext all = queryBuilder.keyword().onFields(fields);

        String[] split = q.split("\\s+");
        for (String s : split) {
            if (s == null || s.isEmpty() || stopWords.contains(s)) continue;
            Query query = all.matching(s).createQuery();
            bool.must(query);
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
}
