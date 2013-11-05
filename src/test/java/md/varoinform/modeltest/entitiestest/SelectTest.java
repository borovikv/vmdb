package md.varoinform.modeltest.entitiestest;

import md.varoinform.model.dao.BranchProxy;
import md.varoinform.modeltest.util.EntityCreator;
import org.junit.*;
import static org.junit.Assert.*;
import md.varoinform.modeltest.TestHibernateBase;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/5/13
 * Time: 11:54 AM
 */
public class SelectTest extends TestHibernateBase {
    private final BranchProxy branchProxy = new BranchProxy();

    @Before
    public void createBranch(){
        EntityCreator.createBranches();
    }
    @Test
    public void selectTest(){
        Long language_id = 1L;
        Long parent_id = 1L;
        List<BranchProxy.BranchProxyView> views = branchProxy.getBranchProxyView(language_id, parent_id);
        System.out.println(views);
        assertEquals(views.size(), 5);
    }
}
