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
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.context.internal.ThreadLocalSessionContext;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public enum  SessionManager {
    instance;

    private ConcurrentMap<String, SessionFactory> sessions = new ConcurrentHashMap<>();
    public static final String DEFAULT = "default";

    public Session getSession() {
        Configurator configurator = new Configurator();
        Configuration config = configurator.configure();
        return  getSession(config);
    }

    public Session getSession(Configuration configuration){
        return getSession(DEFAULT, configuration);
    }

    public Session getSession(String name, Configuration configuration){
        SessionFactory factory = sessions.get(name);
        if(factory != null){
            Session session = factory.getCurrentSession();
            if(session.isOpen()) return session;
            session = factory.openSession();
            ThreadLocalSessionContext.bind(session);
            return session;
        }

        factory = buildSessionFactory(configuration);
        Session session = factory.openSession();
        ThreadLocalSessionContext.bind(session);
        sessions.put(name, factory);
        return session;

    }

    private SessionFactory buildSessionFactory(Configuration configuration) {
        try {
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .applySettings(
                            configuration.getProperties()
                    ).build();
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
            shutdown(s);
        }
    }

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

    @SuppressWarnings("UnusedDeclaration")
    public void closeSession(){
        closeSession(DEFAULT);
    }

    public void closeSession(String name){
        SessionFactory factory = sessions.get(name);
        if (factory != null) {
            Session session = factory.getCurrentSession();
            if (session.isOpen()) session.close();
        }
    }


}
