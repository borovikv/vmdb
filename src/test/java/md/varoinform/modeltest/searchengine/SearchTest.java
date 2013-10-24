package md.varoinform.modeltest.searchengine;

import md.varoinform.model.entities.Brand;
import md.varoinform.model.entities.Contact;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.modeltest.TestHibernateBase;
import md.varoinform.modeltest.util.EntityCreator;
import org.junit.*;

import java.util.ArrayList;
import java.util.Collection;
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
        List<String> brandTitles = new ArrayList<>();
        for (Brand brand : varo.getBrands()) {
            brandTitles.add(brand.getTitle());
        }
        Collections.sort(brandTitles);
        assertEquals(brandTitles.get(1), "techno-design");
        assertEquals(brandTitles.get(0), "Varo");
        System.out.println(varo.branches());
    }

    @Test
    public void testContact(){
        Contact c = (Contact)session.createCriteria(Contact.class).list().get(0);
        System.out.println("*******************************************************************************************");
        System.out.println(c);
    }
}
