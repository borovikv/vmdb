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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SessionManager {
    private static ConcurrentMap<String, Pair> sessions = new ConcurrentHashMap<>();
    public static final String DEFAULT = "default";

    private SessionManager(){}

    public static Session getSession(){
        return  getSession(null);
    }

    public static Session getSession(Configuration configuration){
        if (configuration == null){
            configuration = new Configurator().configure();
        }
        return getSession(DEFAULT, configuration);
    }

    public static Session getSession(String name, Configuration configuration){
        Pair pair = sessions.get(name);
        if(pair != null){
            return pair.session;
        }
        SessionFactory factory = buildSessionFactory(configuration);
        Session session = factory.openSession();
        sessions.put(name, new Pair(factory, session));
        return session;

    }

    private static SessionFactory buildSessionFactory(Configuration configuration) {
        try {
            ServiceRegistryBuilder serviceRegistryBuilder = new ServiceRegistryBuilder();
            serviceRegistryBuilder.applySettings(configuration.getProperties());
            ServiceRegistry serviceRegistry = serviceRegistryBuilder.buildServiceRegistry();

            return configuration.buildSessionFactory(serviceRegistry);
        }
        catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }


    @SuppressWarnings("UnusedDeclaration")
    public static void shutdownAll(){
        for (String s : sessions.keySet()) {
            shutdown(s);
        }
    }

    public static void shutdown(){
        shutdown(DEFAULT);
    }

    public static void shutdown(String name){
        Pair pair = sessions.remove(name);
        if (pair != null) pair.shutdown();
    }


    @SuppressWarnings("UnusedDeclaration")
    public static void closeAllSessions(){
        for (String s : sessions.keySet()) {
            closeSession(s);
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public static void closeSession(){
        closeSession(DEFAULT);
    }

    public static void closeSession(String name){
        Pair pair = sessions.get(name);
        if (pair != null) pair.closeSession();
    }


    private static class Pair{
        public SessionFactory sessionFactory;
        public Session session;

        private Pair(SessionFactory sessionFactory, Session session) {
            this.sessionFactory = sessionFactory;
            this.session = session;
        }

        public void shutdown(){
            closeSession();
            closeSessionFactory();
        }

        public void closeSession(){
            if (session != null && session.isOpen()) {
                session.close();
                session = null;
            }
        }

        public void closeSessionFactory() {
            if (sessionFactory != null) {
                sessionFactory.close();
                sessionFactory = null;
            }
        }
    }


}
