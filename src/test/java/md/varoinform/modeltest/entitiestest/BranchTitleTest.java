package md.varoinform.modeltest.entitiestest;

import md.varoinform.model.entities.Branch;
import md.varoinform.model.entities.BranchTitle;
import org.hibernate.criterion.Restrictions;
import org.junit.*;
import static org.junit.Assert.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/4/13
 * Time: 10:47 AM
 */
public class BranchTitleTest extends TestEntitiesBase {

    @Before
    public void before() {
        transaction = session.beginTransaction();
        Branch b = new Branch();
        session.save(b);
        for (int i = 0; i < 3; i++) {
            BranchTitle title = new BranchTitle();
            title.setTitle("test-" + i);
            title.setBranch(b);
            session.save(title);
        }
        transaction.commit();
    }

    @Test
    public void testTitle(){
        List<BranchTitle> titles = session.createCriteria(BranchTitle.class).list();
        for (BranchTitle title : titles) {
            System.out.println(title + " + branch = " + title.getBranch());
        }
        assertTrue(titles.size() == 3);
    }


    @Test
    public void testBranchGetTitles(){
        List<Branch> branches = session.createCriteria(Branch.class).add(Restrictions.eq("id", 1L)).list();
        assertTrue(branches.size() > 0);
        Branch branch = branches.get(0);
        session.refresh(branch);
        List<BranchTitle> titles = branch.getTitles();
        System.out.println(titles);
        assertEquals(titles.size(), 3);
    }
}
