package md.varoinform.modeltest.entitiestest;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.*;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/3/13
 * Time: 1:36 PM
 */
public class TestEntitiesBase {
    private static Configuration configuration;
    protected static Session session;
    private static SessionFactory sessionFactory;
    private static ServiceRegistry serviceRegistry;
    protected static Transaction transaction;

    @Before
    public void init() throws HibernateException {
        configuration = new Configuration();
        configuration.configure(new File("/home/drifter/development/idea/VaroDB/src/main/resources/test_hibernate.cfg.xml"));
        serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties())
                .buildServiceRegistry();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        session =  sessionFactory.openSession();
    }

    @After
    public void  tearDown(){
        session.close();
        sessionFactory.close();
    }
}
