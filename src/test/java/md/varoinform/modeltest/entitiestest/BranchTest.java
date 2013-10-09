package md.varoinform.modeltest.entitiestest;

import md.varoinform.model.dao.TransactionDaoHibernateImpl;
import md.varoinform.model.entities.Branch;
import org.hibernate.Transaction;
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
    private TransactionDaoHibernateImpl<Branch, Long> dao;

    public BranchTest() {
        this.dao = getDao();
    }

    @Before
    public void setUp() throws Exception {

        Branch root = new Branch();

        dao.create(root);

        for (long i = 2; i< 10; i++){
            Branch b = new Branch();
            b.setParent(root);
            dao.create(b);
        }
    }

    private TransactionDaoHibernateImpl<Branch, Long> getDao() {
        return new TransactionDaoHibernateImpl<Branch, Long>(Branch.class);
    }

    @Test
    public void BranchCreate() throws Exception{
        List<Branch> branches = dao.getAll();
        assertEquals(branches.size(), 9);
    }



    @Test
    public void getChildren(){
        Branch r  = dao.read(1L);
        session.refresh(r);
        assertNotNull(r.getChildren());
        assertEquals(r.getChildren().size(), 8);

    }

    @Test
    public void getParent(){
        Branch b  = dao.read(2L);
        assertEquals((long)b.getParent().getId(), 1);
    }

}
