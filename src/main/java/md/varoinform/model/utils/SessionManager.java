package md.varoinform.model.utils;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/2/13
 * Time: 5:21 PM
 */
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.SessionFactoryObserver;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.internal.StandardServiceRegistryImpl;
import org.hibernate.cfg.Configuration;
import org.hibernate.context.internal.ThreadLocalSessionContext;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public enum  SessionManager {
    instance;

    private final ConcurrentMap<String, SessionFactory> sessions = new ConcurrentHashMap<>();

    public Session getSession(Configuration configuration){
        String path = configuration.getProperty("hibernate.connection.url");
        SessionFactory factory = sessions.get(path);
        if(factory != null){
            Session session = factory.openSession();
            ThreadLocalSessionContext.bind(session);
            return session;
        }
        factory = buildSessionFactory(configuration);
        Session session = factory.openSession();
        ThreadLocalSessionContext.bind(session);
        sessions.put(path, factory);
        return session;

    }

    private SessionFactory buildSessionFactory(Configuration configuration) {
        try {
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .applySettings(
                            configuration.getProperties()
                    ).build();

            configuration.setSessionFactoryObserver(new SessionFactoryObserver() {
                @Override
                public void sessionFactoryCreated(SessionFactory sessionFactory) {

                }

                @Override
                public void sessionFactoryClosed(SessionFactory sessionFactory) {
                    ((StandardServiceRegistryImpl) sessionFactory.getSessionFactoryOptions().getServiceRegistry()).destroy();
                }
            });

            return configuration.buildSessionFactory(registry);
        }
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }


    public void shutdownAll(){
        for (String s : sessions.keySet()) {
            SessionFactory factory = sessions.remove(s);
            if (factory != null) {
                factory.close();
            }
        }
    }

    public void clearCache(){
        for (String s : sessions.keySet()) {
            SessionFactory factory = sessions.get(s);
            factory.getCache().evictAllRegions();
        }
    }

}
