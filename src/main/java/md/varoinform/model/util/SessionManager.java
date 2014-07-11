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
import org.hibernate.SessionFactoryObserver;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.internal.StandardServiceRegistryImpl;
import org.hibernate.cfg.Configuration;
import org.hibernate.context.internal.ThreadLocalSessionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public enum  SessionManager {
    instance;

    private final ConcurrentMap<String, SessionFactory> sessions = new ConcurrentHashMap<>();
    private final List<Session> sessionList = new ArrayList<>();
    public Session getSession() {
        Configurator configurator = new Configurator();
        Configuration config = configurator.configure();
        return  getSession(config);
    }

    public Session getSession(Configuration configuration){
        String path = configuration.getProperty("hibernate.connection.url");
        SessionFactory factory = sessions.get(path);
        if(factory != null){
            Session session = factory.getCurrentSession();
            if( !session.isOpen()) {
                session = factory.openSession();
                ThreadLocalSessionContext.bind(session);
            }
            sessionList.add(session);
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
        for (int i = 0; i < sessionList.size(); i++) {
            Session session = sessionList.get(i);
            if (session!=null && session.isOpen()) {
                session.clear();
                session.close();
            }
            sessionList.set(i, null);
        }
        for (String s : sessions.keySet()) {
            shutdown(s);
        }
    }

    @SuppressWarnings("WeakerAccess")
    public void shutdown(String name){
        SessionFactory factory = sessions.remove(name);
        if (factory != null) {
            factory.close();
        }
    }


    @SuppressWarnings("UnusedDeclaration")
    public void closeAllSessions(){
        for (String s : sessions.keySet()) {
            closeSession(s);
        }
    }


    @SuppressWarnings("WeakerAccess")
    public void closeSession(String name){
        SessionFactory factory = sessions.get(name);
        if (factory != null) {
            Session session = factory.getCurrentSession();
            if (session.isOpen()) session.close();
        }
    }


}
