package md.varoinform.modeltest.entitiestest;

import md.varoinform.model.entities.Branch;
import md.varoinform.model.entities.BranchTitle;
import md.varoinform.model.entities.Language;
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
        Language l1 = new Language("rus");
        Language l2 = new Language("rom");
        Language l3 = new Language("en");
        session.save(l1);
        session.save(l2);
        session.save(l3);
        Language[] languages = {l1, l2, l3};
        for (int i = 0; i < 3; i++) {
            BranchTitle title = new BranchTitle();
            title.setTitle("test-" + i);
            title.setBranch(b);
            title.setLanguage(languages[i]);
            session.save(title);
        }
        transaction.commit();
    }

    @Test
    public void testTitle(){
        @SuppressWarnings("unchecked")
        List<BranchTitle> titles = session.createCriteria(BranchTitle.class).list();
        for (BranchTitle title : titles) {
            System.out.println(title + " + branch = " + title.getBranch());
        }
        assertTrue(titles.size() == 3);
    }


    @Test
    public void testBranchGetTitles(){
        Branch branch = getBranch(1L);
        List<BranchTitle> titles = branch.getTitles();
        System.out.println(titles);
        assertEquals(titles.size(), 3);
    }

    private Branch getBranch(long id) {
        @SuppressWarnings("unchecked")
        List<Branch> branches = session.createCriteria(Branch.class).add(Restrictions.eq("id", id)).list();
        assertTrue(branches.size() > 0);
        Branch branch = branches.get(0);
        session.refresh(branch);
        return branch;
    }


    @Test
    public void testGetTitle(){
        @SuppressWarnings("unchecked")
        List<Language> languages = session.createCriteria(Language.class).list();
        assertFalse(languages.isEmpty());
        Branch b = getBranch(1L);
        System.out.println("\n------------------------------------------------------------------------------------");
        Language rus = null;
        for (Language language : languages) {
            System.out.println(b.getTitle(language));
            if (language.getTitle().equals("rus")){
                rus = language;
            }
        }

        assertEquals(b.getTitle(rus), "test-0");
    }
}
