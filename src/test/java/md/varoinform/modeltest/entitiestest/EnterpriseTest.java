package md.varoinform.modeltest.entitiestest;

import md.varoinform.model.dao.TransactionDaoHibernateImpl;
import md.varoinform.model.entities.Branch;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.entities.GProduce;
import md.varoinform.model.entities.Good;
import md.varoinform.modeltest.TestHibernateBase;
import org.junit.*;
import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/8/13
 * Time: 10:55 AM
 */
public class EnterpriseTest extends TestHibernateBase {
    private final Date check;
    private final Date create;
    private final Date change;
    private final TransactionDaoHibernateImpl<Enterprise, Long> enterpriseDao;
    private final TransactionDaoHibernateImpl<Branch, Long> branchDao;
    private final TransactionDaoHibernateImpl<Good, Long> goodDao;
    private final TransactionDaoHibernateImpl<GProduce, Long> gproduceDao;

    public EnterpriseTest() {
        enterpriseDao = new TransactionDaoHibernateImpl<>(Enterprise.class);
        branchDao = new TransactionDaoHibernateImpl<>(Branch.class);
        goodDao = new TransactionDaoHibernateImpl<>(Good.class);
        gproduceDao = new TransactionDaoHibernateImpl<>(GProduce.class);
        check = getDate(-1);
        create = getDate(-2);
        change = getDate(-3);
    }

    private Date getDate(int inc) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, inc);
        return calendar.getTime();
    }

    @Before
    public void before(){
        Enterprise enterprise = createEnterprise();
        createGoodForBranchAndEnterprise(enterprise);
        createGoodForBranchAndEnterprise(enterprise);
    }

    private Enterprise createEnterprise() {
        Enterprise enterprise = new Enterprise();

        enterprise.setCheckDate(check);
        enterprise.setCreation(create);
        enterprise.setLastChange(change);

        enterprise.setForeignCapital(Boolean.FALSE);
        enterprise.setLogo("logo");
        enterprise.setYpUrl("yp.md");
        enterprise.setWorkplaces(40);
        enterpriseDao.create(enterprise);
        return enterprise;
    }

    private void createGoodForBranchAndEnterprise(Enterprise enterprise) {
        Branch branch = createBranch();
        Good good = crateGood(branch);
        createGProduce(enterprise, good);
    }

    private Branch createBranch() {
        Branch branch = new Branch();
        branchDao.create(branch);
        return branch;
    }

    private Good crateGood(Branch branch) {
        Good good = new Good();
        good.setBranch(branch);
        goodDao.create(good);
        return good;
    }

    private void createGProduce(Enterprise enterprise, Good good) {
        GProduce gProduce = new GProduce();
        gProduce.setGood(good);
        gProduce.setEnterprise(enterprise);
        gproduceDao.create(gProduce);
    }

    @Test
    public void testDate(){
        Enterprise e = enterpriseDao.read(1L);
        Date check = e.getCheckDate();
        Date create = e.getCreation();
        Date change = e.getLastChange();
        System.out.println("check -" + check + "\ncreate - " + create + "\nchange - " + change);
        assertEquals(check, this.check);
        assertEquals(create, this.create);
        assertEquals(change, this.change);

    }

    @Test
    public void testBranch(){
        Enterprise e = enterpriseDao.read(1L);
        session.refresh(e);
        List<Branch> bs = e.branches();
        System.out.println(bs);
        assertEquals(bs.size(), 2);
    }

    @Test
    public void testGoods(){
        Enterprise e = enterpriseDao.read(1L);
        session.refresh(e);
        List<GProduce> goods = e.getGoods();
        System.out.println(goods);
        assertEquals(goods.size(), 2);
    }

}
