package md.varoinform.modeltest.entitiestest;

import md.varoinform.model.dao.TransactionDaoHibernateImpl;
import md.varoinform.model.entities.TreeNode;
import md.varoinform.modeltest.TestHibernateBase;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;
/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/2/13
 * Time: 5:01 PM
 */
public class TreeNodeTest extends TestHibernateBase {
    private final TransactionDaoHibernateImpl<TreeNode, Long> dao;

    public TreeNodeTest() {
        this.dao = getDao();
    }

    @Before
    public void setUp() throws Exception {

        TreeNode root = new TreeNode();

        dao.save(root);

        for (long i = 2; i< 10; i++){
            TreeNode b = new TreeNode();
            b.setParent(root.getId());
            dao.save(b);
        }
    }

    private TransactionDaoHibernateImpl<TreeNode, Long> getDao() {
        return new TransactionDaoHibernateImpl<>(TreeNode.class);
    }

    @Test
    public void BranchCreate() throws Exception{
        List<TreeNode> treeNodes = dao.getAll();
        assertEquals(treeNodes.size(), 9);
    }



    @Test
    public void getChildren(){
        TreeNode r  = dao.read(1L);
        session.refresh(r);
        /*assertNotNull(r.getChildren());
        assertEquals(r.getChildren().size(), 8);
        */
    }

    @Test
    public void getParent(){
        TreeNode b  = dao.read(2L);
        assertEquals((long)b.getParent(), 1);
    }

}
