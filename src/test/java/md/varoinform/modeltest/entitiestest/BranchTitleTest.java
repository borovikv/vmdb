package md.varoinform.modeltest.entitiestest;

import md.varoinform.model.dao.TransactionDaoHibernateImpl;
import md.varoinform.model.entities.Branch;
import md.varoinform.model.entities.BranchTitle;
import md.varoinform.model.entities.Language;
import org.hibernate.Transaction;
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
    private TransactionDaoHibernateImpl<Branch, Long> daoBranch;
    private TransactionDaoHibernateImpl<Language, Long> daoLanguage;
    private TransactionDaoHibernateImpl<BranchTitle, Long> daoBranchTitle;
    public BranchTitleTest() {
        daoBranch = new TransactionDaoHibernateImpl<Branch, Long>(Branch.class);
        daoLanguage = new TransactionDaoHibernateImpl<Language, Long>(Language.class);
        daoBranchTitle = new TransactionDaoHibernateImpl<BranchTitle, Long>(BranchTitle.class);
    }

    @Before
    public void before() {
        Branch b = new Branch();
        daoBranch.create(b);
        Language l1 = createLanguage("rus");
        Language l2 = createLanguage("rom");
        Language l3 = createLanguage("eng");

        Language[] languages = {l1, l2, l3};
        for (int i = 0; i < 3; i++) {
            BranchTitle title = new BranchTitle();
            title.setTitle("test-" + i);
            title.setBranch(b);
            title.setLanguage(languages[i]);
            daoBranchTitle.create(title);
        }
    }

    private Language createLanguage(String title) {
        Language l1 = new Language(title);
        daoLanguage.create(l1);
        return l1;
    }

    @Test
    public void testTitle(){
        List<BranchTitle> titles = daoBranchTitle.getAll();
        for (BranchTitle title : titles) {
            System.out.println(title + " + branch = " + title.getBranch());
        }
        assertTrue(titles.size() == 3);
    }


    @Test
    public void testBranchGetTitles(){
        Branch branch = getBranch();
        List<BranchTitle> titles = branch.getTitles();
        System.out.println(titles);
        assertEquals(titles.size(), 3);
    }

    private Branch getBranch() {
        Branch branch = daoBranch.read(1L);
        session.refresh(branch);
        return branch;
    }

    @Test
    public void testGetTitle(){
        @SuppressWarnings("unchecked")
        List<Language> languages = session.createCriteria(Language.class).add(Restrictions.eq("title", "rus")).list();
        assertFalse(languages.isEmpty());
        Language rus = languages.get(0);

        Branch b = getBranch();
        assertEquals(b.getTitle(rus), "test-0");
    }
}
