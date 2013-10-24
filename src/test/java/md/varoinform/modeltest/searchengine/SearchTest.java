package md.varoinform.modeltest.searchengine;

import md.varoinform.model.entities.Enterprise;
import md.varoinform.modeltest.TestHibernateBase;
import md.varoinform.modeltest.util.EntityCreator;
import org.junit.*;

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
    }
}
