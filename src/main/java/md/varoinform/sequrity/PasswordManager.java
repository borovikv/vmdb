package md.varoinform.sequrity;

import md.varoinform.model.util.SessionManager;
import md.varoinform.sequrity.exception.Error;
import md.varoinform.sequrity.exception.PasswordException;
import md.varoinform.util.PreferencesHelper;
import md.varoinform.util.StringConverter;
import org.hibernate.Session;


/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/12/13
 * Time: 5:14 PM
 */
public class PasswordManager {
    private final PreferencesHelper preferencesHelper = new PreferencesHelper();
    private static String password = "";

    public String getDBPassword(String uid) throws PasswordException {
        byte[] encryptedPassword = preferencesHelper.getDBPassword();
        if (encryptedPassword == null) throw new PasswordException(Error.PASSWORD_NOT_EXIST_ERROR);

        String password = decryptPassword(uid, encryptedPassword);

        if( !testConnection(password)) throw new PasswordException(Error.VALIDATION_ERROR);

        return password;
    }

    public String decryptPassword(String uid, byte[] encryptedPassword) {
        Cypher cypher = new Cypher();
        byte[] key = cypher.createKey(uid);
        return cypher.decrypt(encryptedPassword, key);
    }

    //ToDo:real implementation testConnection (test connection to db)
    private boolean testConnection(String password) {
        PasswordManager.password = password;
        try {
            Session session = SessionManager.instance.getSession();
            session.beginTransaction().rollback();
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    public void setDBPassword(String uid, byte[] encryptedPassword) throws PasswordException {
        String password = decryptPassword(uid, encryptedPassword);
        if( !testConnection(password)) throw new PasswordException(Error.VALIDATION_ERROR);
        preferencesHelper.setDBPassword(encryptedPassword);
    }

    public void setDBPassword(String uid, String encryptedPassword) throws PasswordException {
        byte[] bytes = StringConverter.getBytesFromHexString(encryptedPassword);
        setDBPassword(uid, bytes);
    }

    public static String getPassword() {
        return password;
    }
}
