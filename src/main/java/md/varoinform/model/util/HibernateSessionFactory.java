package md.varoinform.model.util;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/2/13
 * Time: 5:21 PM
 */
import md.varoinform.model.Configurator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class HibernateSessionFactory {

    private static SessionFactory sessionFactory;
    private static Session session;

    private HibernateSessionFactory(){}

    private static SessionFactory buildSessionFactory(Configuration configuration) {
        try {
            ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            return sessionFactory;
        }
        catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static SessionFactory getSessionFactory(Configuration configuration) {
        if (sessionFactory != null ){
            return sessionFactory;
        }
        return buildSessionFactory(configuration);
    }

    public static Session getSession(Configuration configuration){
        if(session != null){
            return session;
        }
        if (configuration == null){
            configuration = new Configurator().configure();
        }
        session = HibernateSessionFactory.getSessionFactory(configuration).openSession();
        return session;
    }

    public static Session getSession(){
        return  getSession(null);
    }

    public static void shutdown(){
        closeSession();
        closeSessionFactory();
    }

    public static void closeSession(){
        if (session != null && session.isOpen()) {
            session.close();
        }
    }

    private static void closeSessionFactory() {
        // Чистит кеш и закрывает соединение с БД
        if (sessionFactory != null) {
            sessionFactory.close();
            session = null;
            sessionFactory = null;
        }
    }

}
