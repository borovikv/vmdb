package md.varoinform.model.utils;

import md.varoinform.Settings;
import org.hibernate.cfg.Configuration;

/**
 * Created by vladimir on 24.04.15.
 *
 */
public class DefaultClosableSession extends ClosableSession{
    private static final Configuration cfg;
    static {

        String pathToDb = Settings.pathToDB().toString();
        cfg = new Configurator(pathToDb, "test", "test").configureWithoutIndex();
    }

    public DefaultClosableSession() {
        super(cfg);
    }

    public DefaultClosableSession(Configuration cfg){
        super(cfg == null ? DefaultClosableSession.cfg : cfg);
    }
}
