package md.varoinform.modeltest.entitiestest;

import md.varoinform.model.entities.Branch;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.entities.GProduce;
import md.varoinform.model.entities.Good;
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
public class EnterpriseTest extends TestEntitiesBase {
    Date check;
    Date create;
    Date change;
    @Before
    public void before(){
        check = getDate(-1);
        create = getDate(-2);
        change = getDate(-3);

        Enterprise enterprise = new Enterprise();

        enterprise.setCheckDate(check);
        enterprise.setCreation(create);
        enterprise.setLastChange(change);

        enterprise.setForeingCapital(Boolean.FALSE);
        enterprise.setLogo("logo");
        enterprise.setYpUrl("yp.md");
        enterprise.setWorkplaces(40);

        Branch branch = new Branch();
        Good good = new Good();
        good.setBranch(branch);
        GProduce gProduce = new GProduce();
        gProduce.setGood(good);
        gProduce.setEnterprise(enterprise);

        Branch branch1 = new Branch();
        Good good1 = new Good();
        good1.setBranch(branch1);
        GProduce gProduce1 = new GProduce();
        gProduce1.setGood(good);
        gProduce1.setEnterprise(enterprise);

        session.beginTransaction();
        session.save(enterprise);
        session.save(branch);
        session.save(branch1);
        session.save(good);
        session.save(good1);
        session.save(gProduce);
        session.save(gProduce1);
        session.getTransaction().commit();
    }

    @Test
    public void testDate(){
        Enterprise e = getEnterprise();
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
        Enterprise e = getEnterprise();
        session.refresh(e);
        List<Branch> bs = e.branches();
        System.out.println(bs);
        assertEquals(bs.size(), 2);
    }

    @Test
    public void testGoods(){
        Enterprise e = getEnterprise();
        session.refresh(e);
        List<GProduce> goods = e.getGoods();
        System.out.println(goods);
        assertEquals(goods.size(), 2);
    }
    private Enterprise getEnterprise() {
        List<Enterprise> enterprises = session.createCriteria(Enterprise.class).list();
        assertFalse(enterprises.isEmpty());
        return enterprises.get(0);
    }

    private Date getDate(int inc) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, inc);
        return calendar.getTime();
    }
}
