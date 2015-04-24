package md.varoinform.model.utils;

import md.varoinform.Settings;
import md.varoinform.sequrity.PasswordManager;
import org.hibernate.cfg.Configuration;

/**
 * Created by vladimir on 24.04.15.
 *
 */
public class DefaultClosableSession extends ClosableSession{
    private static final Configuration cfg;
    static {

        cfg = new Configurator(Settings.pathToDB().toString(), PasswordManager.getPassword()).configure();
    }

    public DefaultClosableSession() {
        super(cfg);
    }

    public DefaultClosableSession(Configuration cfg){
        super(cfg == null ? DefaultClosableSession.cfg : cfg);
    }
}
