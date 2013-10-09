package md.varoinform.modeltest.entitiestest;

import md.varoinform.model.util.HibernateSessionFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.*;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/3/13
 * Time: 1:36 PM
 */
public class TestEntitiesBase {
    protected static Session session;

    @Before
    public void init() throws HibernateException {
        session = HibernateSessionFactory.getSession(new File("/home/drifter/development/idea/VaroDB/src/main/resources/test_hibernate.cfg.xml"));
    }

    @After
    public void  tearDown(){
        HibernateSessionFactory.shutdown();
    }
}
