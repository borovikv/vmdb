package md.varoinform.model.search;

import md.varoinform.model.util.SessionManager;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 6/25/14
 * Time: 9:44 AM
 */
public class QueryAnalyzer {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern CLEANED_TEXT_PATTERN = Pattern.compile("\\({0,1}(\\w+\\s)*(\\w+)\\){0,1}", Pattern.UNICODE_CHARACTER_CLASS);
//    private static final Pattern URL_PATTERN = Pattern.compile("^(www)$|^((www\\.){0,1}\\S+\\.[a-z]{2,6})$", Pattern.CASE_INSENSITIVE);
    private final String query;

    public QueryAnalyzer(String query) {
        this.query = query;
    }

    public List<String> getTokens() {
        FullTextSession fullTextSession = Search.getFullTextSession(SessionManager.getSession());
        Analyzer analyzer = fullTextSession.getSearchFactory().getAnalyzer("customanalyzer");
        QueryParser parser = new QueryParser(Version.LUCENE_35, "title", analyzer);
        List<String> tokenized = new ArrayList<>();
        List<String> emails = new ArrayList<>();
        String searchString = prepareString(query, emails);
        tokenized.addAll(emails);
        if (searchString.isEmpty()) return tokenized;

        try {
            Query query = parser.parse(searchString);
            String cleanedText = query.toString("title");
            Matcher matcher = CLEANED_TEXT_PATTERN.matcher(cleanedText);
            while (matcher.find()) {
                String word = matcher.group(2);
                tokenized.add(word.trim());
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tokenized;
    }

    private String prepareString(String searchString, List<String> emails) {
        StringBuilder result = new StringBuilder();
        for (String s : searchString.split("\\s")) {
            String word = s.trim();
            if (EMAIL_PATTERN.matcher(word).matches()){
                emails.add(word);
            } else {
                result.append(word);
                result.append(" ");
            }
        }
        return result.toString().trim().replaceAll("[-\\.]", " ");
    }
}
