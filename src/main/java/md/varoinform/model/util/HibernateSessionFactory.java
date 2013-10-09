package md.varoinform.model.util;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/2/13
 * Time: 5:21 PM
 */
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import java.io.File;

public class HibernateSessionFactory {

    private static SessionFactory sessionFactory;
    private static ServiceRegistry serviceRegistry;
    private static Configuration configuration;
    private static Session session;

    private HibernateSessionFactory(){}

    private static SessionFactory buildSessionFactory(Configuration configuration) {
        HibernateSessionFactory.configuration = configuration;
        try {
            serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            return sessionFactory;
        }
        catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static SessionFactory rebuildSessionFactory(Configuration configuration){
        if (configuration == HibernateSessionFactory.configuration && sessionFactory != null){
            return sessionFactory;
        }
        closeSessionFactory();
        return buildSessionFactory(configuration);
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
        session = HibernateSessionFactory.getSessionFactory(configuration).openSession();
        return session;
    }

    public static Session getSession(File resource) {
        Configuration configuration = new Configuration();
        configuration.configure(resource);
        return getSession(configuration);
    }

    public static Session getSession(){
        Configuration configuration = new Configuration();
        configuration.configure();
        return  getSession(configuration);
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
