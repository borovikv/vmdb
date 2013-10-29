package md.varoinform.modeltest.searchengine;

import md.varoinform.model.entities.*;
import md.varoinform.model.search.SearchEngine;
import md.varoinform.modeltest.TestHibernateBase;
import md.varoinform.modeltest.util.EntityCreator;
import org.hibernate.criterion.Restrictions;
import org.junit.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/9/13
 * Time: 4:33 PM
 */
public class SearchTest extends TestHibernateBase {
    List<Enterprise> enterprises;

    private void testQuery(String query, String... names) {
        testQuery(query, -1, names);
    }

    private void testQuery(String query, int resultLength, String... names) {
        SearchEngine searchEngine = new SearchEngine(session);
        List<Enterprise> searchResult = searchEngine.search(query);

        System.out.println(query);
        printResult(searchResult);

        if (resultLength >= 0){
            assertEquals(searchResult.size(), resultLength);
        } else {
            assertFalse(searchResult.isEmpty());
        }
        for (int i = 0; i < names.length; i++) {
            Enterprise e = getEnterpriseByName(names[i]);
            assertEquals(e, searchResult.get(i));
        }
    }

    private void printResult(List<Enterprise> searchResult) {
        System.out.println("-------------------------------------------------------------");
        for (Enterprise enterprise : searchResult) {
            for (GProduce gProduce : enterprise.getGoods()) {
                Good g = gProduce.getGood();
                session.refresh(g);
                session.refresh(g.getBranch());
                System.out.println("ent = " + enterprise.getTitles() + " goods = " + g.getTitles() + " branch " + g.getBranch().getTitles());
            }
        }
        System.out.println(searchResult);
        System.out.println(searchResult.size());
        System.out.println("-------------------------------------------------------------");
    }

    private List<Enterprise> getEnterprisesByName(String... names) {
        List<Enterprise> result = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            result.add(getEnterpriseByName(names[i]));
        }
        return result;
    }

    private Enterprise getEnterpriseByName(String name) {
        List results = session.createCriteria(EnterpriseTitle.class).add(Restrictions.eq("title", name)).list();
        assertFalse(results.isEmpty());
        EnterpriseTitle title = (EnterpriseTitle)results.get(0);
        return title.getContainer();
    }

    @Before
    public void createEnterprises(){
        enterprises = EntityCreator.createEnterprises();
        refresh(enterprises);
    }

    private void refresh(List<Enterprise> enterprises) {
        for (Enterprise enterprise : enterprises) {
            session.refresh(enterprise);
            System.out.println(enterprise);
        }
    }

    @Test
    public void testIsCreated(){
        assertEquals(enterprises.size(), 3);
        Enterprise varo = enterprises.get(0);
        assertEquals(varo.getTitles().get(0).getTitle(), "Varo");

        List<String> brandTitles = getBrandsNameSortedList(varo);
        assertEquals(brandTitles.get(1), "techno-design");
        assertEquals(brandTitles.get(0), "Varo");
        assertEquals(varo.branches().size(), 3);
    }

    private List<String> getBrandsNameSortedList(Enterprise enterprise) {
        List<String> brandTitles = new ArrayList<>();
        for (Brand brand : enterprise.getBrands()) {
            brandTitles.add(brand.getTitle());
        }
        Collections.sort(brandTitles);
        return brandTitles;
    }

    @Test
    public void testSearchByName(){
        //["Varo"], ["Polygraph", "Полиграф"], ["house&Polygraph"]
        testQuery("Varo", "Varo");
        testQuery("Polygraph", "Polygraph", "house&Polygraph");
        testQuery("house&Polygraph", "house&Polygraph");
    }

    @Test
    public void testSearchByGood(){
        testQuery("плакаты макеты", "Varo", "Polygraph");
        testQuery("гвозди", "house&Polygraph");
    }

    @Test
    public void testMultipleTypeQuery(){
        testQuery("techno-design плакаты", "Varo");
        testQuery("techno-design утюги");
        testQuery("макеты Г.Тудор", "Polygraph", "Varo");
        testQuery("плакаты Г.Тудор", "Varo");
    }

    @Test
    public void testAnalyzedSearch(){
        testQuery("плакаты и макеты", 2, "Varo", "Polygraph");
        testQuery("гвоздь", "house&Polygraph");
    }

}
