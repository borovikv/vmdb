package md.varoinform.modeltest.searchengine;

import md.varoinform.model.entities.Brand;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.search.SearchEngine;
import md.varoinform.modeltest.TestHibernateBase;
import md.varoinform.modeltest.util.EntityCreator;
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

    private void testQuery(String query, String... names) {
        SearchEngine searchEngine = new SearchEngine();
        List<Enterprise> searchResult = searchEngine.search(query);
        assertNotEquals(searchResult.size(), 0);
        assertArrayEquals(searchResult.toArray(), getEnterprisesByName(names));
    }

    private Object[] getEnterprisesByName(String... names) {
        Object[] result = new Object[names.length];
        for (int i = 0; i < names.length; i++) {
            result[i] = getEnterpriseByName(names[i]);
        }
        return result;
    }

    private Enterprise getEnterpriseByName(String name) {
        return null;
    }

    @Test
    public void testSearchByGood(){
        testQuery("плакаты и макеты", "Varo", "Polygraph");
        testQuery("утюг", "house&Polygraph");
    }

    @Test
    public void testMultipleTypeQuery(){
        testQuery("techno-design плакаты", "Varo");
        testQuery("techno-design утюги");
        testQuery("макеты Г.Тудор", "Varo", "Polygraph");
        testQuery("плакаты Г.Тудор", "Varo");
    }



}
