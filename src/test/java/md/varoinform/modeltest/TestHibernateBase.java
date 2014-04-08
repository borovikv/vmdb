package md.varoinform.modeltest;

import md.varoinform.model.Configurator;
import md.varoinform.model.util.SessionManager;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.junit.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/3/13
 * Time: 1:36 PM
 */
public class TestHibernateBase {
    protected static Session session;

    @Before
    public void init() throws HibernateException {
        Configurator configurator = new Configurator("/home/drifter/development/idea/VaroDB/database/DBTEST");
        Configuration cfg = configurator.getConfiguration();
        configurator.showSql(cfg, false);
        configurator.setAuto(cfg, "save");
        session = SessionManager.getSession(cfg);
    }

    @After
    public void  tearDown(){
        SessionManager.shutdown();
    }
}
