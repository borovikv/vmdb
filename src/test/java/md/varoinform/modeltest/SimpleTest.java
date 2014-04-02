package md.varoinform.modeltest;

import md.varoinform.model.dao.BranchDao;
import md.varoinform.model.dao.EnterpriseDao;
import md.varoinform.model.dao.GenericDaoHibernateImpl;
import md.varoinform.model.entities.Branch;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.util.SessionManager;
import org.hibernate.Session;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/2/14
 * Time: 2:37 PM
 */
public class SimpleTest {
    public static void main(String[] args) {

        System.out.println(BranchDao.getRoot());

    }
}
