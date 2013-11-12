package md.varoinform.modeltest.entitiestest;

import md.varoinform.modeltest.util.BranchProxy;
import md.varoinform.model.dao.EnterpriseDao;
import md.varoinform.model.dao.GenericDaoHibernateImpl;
import md.varoinform.model.entities.Branch;
import md.varoinform.model.entities.BranchTitle;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.modeltest.util.EntityCreator;
import org.junit.*;
import static org.junit.Assert.*;
import md.varoinform.modeltest.TestHibernateBase;

import java.util.ArrayList;
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
        EntityCreator.createEnterprises();
    }

    @Test
    public void selectBranchTest(){
        Long language_id = 1L;
        Long parent_id = 1L;
        List<BranchProxy.BranchProxyView> views = BranchProxy.getBranchProxyView(language_id, parent_id);
        System.out.println(views);
        assertEquals(views.size(), 1);
    }

    @Test
    public void selectEnterpriseTest(){
        int[] r = {0, 1, 2, 2, 3, 3};
        List<Long> branchIds = new ArrayList<>();
        for (long i = 1; i <= 6; i++) {
            branchIds.add(i);
            printBranches(branchIds);
            List<Enterprise> result = EnterpriseDao.getEnterprisesByBranchId(branchIds);
            printEnterprises(result);
            assertEquals(result.size(), r[(int) i - 1]);
        }
    }

    private void printEnterprises(List<Enterprise> result) {
        for (Enterprise enterprise : result) {
            session.refresh(enterprise);
            System.out.println(enterprise.getTitles());
        }
    }

    private void printBranches(List<Long> branchIds){
        GenericDaoHibernateImpl<Branch, Long> branchDao = new GenericDaoHibernateImpl<>(Branch.class);
        System.out.println("branches==========================================================================");
        for (Long branchId : branchIds) {
            Branch branch = branchDao.read(branchId);
            session.refresh(branch);
            List<BranchTitle> titles = branch.getTitles();
            if (titles.isEmpty()){
                System.out.println("root");
            } else {
                System.out.println(titles.get(0));
            }

        }
        System.out.println("end_branches======================================================================");
    }
}
