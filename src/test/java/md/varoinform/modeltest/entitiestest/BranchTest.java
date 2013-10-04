package md.varoinform.modeltest.entitiestest;

import md.varoinform.model.entities.Branch;
import org.hibernate.criterion.Restrictions;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;
/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/2/13
 * Time: 5:01 PM
 */
public class BranchTest extends TestEntitiesBase{
    @Before
    public void setUp() throws Exception {
        transaction = session.beginTransaction();
        Branch root = new Branch();
        root.setId(1L);
        session.save(root);

        for (long i = 2; i< 10; i++){
            Branch b = new Branch();
            b.setId(i);
            b.setParent(root);
            session.save(b);
        }
        transaction.commit();
    }

    @Test
    public void BranchCreate() throws Exception{
        List<Branch> branches = getBranches();
        assertEquals(branches.size(), 9);
    }

    private List<Branch> getBranches() {
        return session.createCriteria(Branch.class).list();
    }

    @Test
    public void getChildren(){
        Branch r  = (Branch)session.createCriteria(Branch.class).add(Restrictions.eq("id", 1L)).list().get(0);
        session.refresh(r);
        assertNotNull(r.getChildren());
        assertEquals(r.getChildren().size(), 8);

    }

    @Test
    public void getParent(){
        Branch b  = getBranch(2L);
        assertEquals((long)b.getParent().getId(), 1);
    }

    private Branch getBranch(Long id) {
        return (Branch)session.createCriteria(Branch.class).add(Restrictions.eq("id", id)).list().get(0);
    }

}
