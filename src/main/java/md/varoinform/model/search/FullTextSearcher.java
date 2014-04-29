package md.varoinform.model.search;

import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.util.SessionManager;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/25/13
 * Time: 2:44 PM
 */
@SuppressWarnings("UnusedDeclaration")
public class FullTextSearcher extends Searcher {

    private final List<String> stopWords;

    public FullTextSearcher() {
        //createIndex(SessionManager.getSession());
        stopWords = getStopWords();
    }

    public void createIndex(Session session) {
        FullTextSession fullTextSession = Search.getFullTextSession(session);
        try {
            fullTextSession.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Enterprise> search(String q) {
        FullTextSession fullTextSession = Search.getFullTextSession(SessionManager.getSession());
        Transaction tx = fullTextSession.beginTransaction();
        try {
            org.apache.lucene.search.Query query = getLuceneQuery(fullTextSession, q);
            org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery(query, Enterprise.class);
            @SuppressWarnings("unchecked")
            List<Enterprise> result = (List<Enterprise>)hibQuery.list();
            tx.commit();
            return result;
        } catch (Exception ex) {
            tx.rollback();
            return null;
        }
    }

    private Query getLuceneQuery(FullTextSession fullTextSession, String q) throws ParseException {
        QueryBuilder qb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Enterprise.class).get();

        BooleanJunction<BooleanJunction> bool = qb.bool();
        TermMatchingContext all = qb.keyword()
                .onFields("titles.title", "goods.good.titles.title", "goods.good.treeNodes.title.titles.title", "brands.title",
                        "contacts.postalCode", "contacts.houseNumber", "contacts.officeNumber",
                        "contacts.street.titles.title", "contacts.sector.titles.title", "contacts.town.titles.title",
                        "contacts.region.titles.title",
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
        try {
            Path path = getPathToStopWords();
            return Files.readAllLines(path, Charset.defaultCharset());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private Path getPathToStopWords() throws IOException {
        FinderVisitor visitor = new FinderVisitor();
        Files.walkFileTree(Paths.get(""), visitor);
        return visitor.getResult();
    }

    @Override
    public String getName() {
        return "default";
    }

    @Override
    public int compareTo(Searcher o) {
        return -1;
    }

    class FinderVisitor extends SimpleFileVisitor<Path> {
        Path result = null;

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.getFileName().toString().equals("word.txt")) {
                    result = file;
                    return FileVisitResult.TERMINATE;
                }

                return FileVisitResult.CONTINUE;
            }

        public Path getResult() {
            return result;
        }
    }
}
