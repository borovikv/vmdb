package md.varoinform.modeltest.entitiestest;

import md.varoinform.model.dao.TransactionDaoHibernateImpl;
import md.varoinform.model.entities.TreeNode;
import md.varoinform.model.entities.convert.BranchTitle;
import md.varoinform.model.entities.Language;
import md.varoinform.modeltest.TestHibernateBase;
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
public class TreeNodeTitleTest extends TestHibernateBase {
    private final TransactionDaoHibernateImpl<TreeNode, Long> daoBranch;
    private final TransactionDaoHibernateImpl<Language, Long> daoLanguage;
    private final TransactionDaoHibernateImpl<BranchTitle, Long> daoBranchTitle;

    public TreeNodeTitleTest() {
        daoBranch = new TransactionDaoHibernateImpl<>(TreeNode.class);
        daoLanguage = new TransactionDaoHibernateImpl<>(Language.class);
        daoBranchTitle = new TransactionDaoHibernateImpl<>(BranchTitle.class);
    }

    @Before
    public void before() {
        TreeNode b = new TreeNode();
        daoBranch.save(b);
        Language l1 = createLanguage("rus");
        Language l2 = createLanguage("rom");
        Language l3 = createLanguage("eng");

        Language[] languages = {l1, l2, l3};
        for (int i = 0; i < 3; i++) {
            BranchTitle title = new BranchTitle();
            title.setTitle("test-" + i);
            //title.setContainer(b);
            title.setLanguage(languages[i]);
            daoBranchTitle.save(title);
        }
    }

    private Language createLanguage(String title) {
        Language l1 = new Language(title);
        daoLanguage.save(l1);
        return l1;
    }

    @Test
    public void testTitle(){
        List<BranchTitle> titles = daoBranchTitle.getAll();
        for (BranchTitle title : titles) {
            System.out.println(title + " + branch = " + title.getContainerID());
        }
        assertTrue(titles.size() == 3);
    }


    @Test
    public void testBranchGetTitles(){
        //TreeNode treeNode = getBranch();
        //List<BranchTitle> titles = treeNode.getTitles();
        //System.out.println(titles);
        //assertEquals(titles.size(), 3);
    }

    private TreeNode getBranch() {
        TreeNode treeNode = daoBranch.read(1L);
        session.refresh(treeNode);
        return treeNode;
    }

    @Test
    public void testGetTitle(){
        @SuppressWarnings("unchecked")
        List<Language> languages = session.createCriteria(Language.class).add(Restrictions.eq("title", "rus")).list();
        assertFalse(languages.isEmpty());
        Language rus = languages.get(0);

        TreeNode b = getBranch();
        assertEquals(b.title(rus), "test-0");
    }

    @Test
    public void testGetContainer(){
        //BranchTitle branchTitle = daoBranchTitle.read(1L);
        //TreeNode treeNode = daoBranch.read(1L);
        //assertEquals(treeNode.getId(), branchTitle.getContainerID().getId());
    }
}
