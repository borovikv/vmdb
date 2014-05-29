package md.varoinform.model.search;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/24/14
 * Time: 9:51 AM
 */
public class Searchers {
    private static List<Searcher> searchers = new ArrayList<>();

    public static List<Searcher> getSearchers() {
        if (!searchers.isEmpty()) return searchers;
        searchers.add(new BusinessEntityTypeSearcher());
        searchers.add(new ContactPersonSearcher());
        searchers.add(new CreationDateSearcher());
        searchers.add(new EmailsSearcher());
        searchers.add(new ForeingCapitalSearcher());
        searchers.add(new FullTextSearcher());
        searchers.add(new GoodsSearcher());
        searchers.add(new PhoneSearcher());
        searchers.add(new PostalCodeSearcher());
        searchers.add(new BrandsSearcher());
        searchers.add(new RegionSearcher());
        searchers.add(new SectorSearcher());
        searchers.add(new StreetSearcher());
        searchers.add(new TitleSearcher(true));
        searchers.add(new TitleSearcher(false));
        searchers.add(new TownSearcher());
        searchers.add(new UrlsSearcher());
        searchers.add(new WorkplacesSearcher(WorkplacesSearcher.ComparisonType.EQ));
        searchers.add(new WorkplacesSearcher(WorkplacesSearcher.ComparisonType.GTE));
        searchers.add(new WorkplacesSearcher(WorkplacesSearcher.ComparisonType.LTE));
        searchers.add(new YpUrlSearcher());
        return searchers;
    }
}
