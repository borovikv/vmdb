package md.varoinform.sequrity;

import md.varoinform.model.util.ClosableSession;
import md.varoinform.sequrity.exception.*;
import md.varoinform.sequrity.exception.Error;
import md.varoinform.util.PreferencesHelper;
import md.varoinform.util.StringConverter;


/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/12/13
 * Time: 5:14 PM
 */
public class PasswordManager {
    private final PreferencesHelper preferencesHelper = new PreferencesHelper();
    private static String password = "password";

    public String getDBPassword(String uid) throws PasswordException, UnregisteredDBExertion, LockedException {
        byte[] encryptedPassword = preferencesHelper.getDBPassword();
        if (encryptedPassword == null) throw new UnregisteredDBExertion();

        String password;
        try {
            password = decryptPassword(uid, encryptedPassword);
        } catch (CryptographyException e) {
            throw new PasswordException(Error.DECRYPT_ERROR);
        }

        testConnection(password);

        return password;
    }

    public String decryptPassword(String uid, byte[] encryptedPassword) throws CryptographyException {
        Cypher cypher = new Cypher();
        byte[] key = cypher.createKey(uid);
        return cypher.decrypt(encryptedPassword, key);
    }

    private void testConnection(String password) throws PasswordException, LockedException {
        PasswordManager.password = password;
        try (ClosableSession session = new ClosableSession()){
            session.beginTransaction().rollback();
        } catch (Throwable e){
            Throwable e1 = e;
            while ((e1 = e1.getCause()) != null) {
                String message = e1.getMessage();
                System.out.println(message);
                if (message.contains("Wrong user name or password [28000-173]")){
                    throw new PasswordException(Error.VALIDATION_ERROR);
                } else if (message.contains("Locked by another process")){
                    throw new LockedException(Error.LOCKED_ERROR);
                }
            }
            System.exit(-1);
        }
    }

    public void setDBPassword(String uid, byte[] encryptedPassword) throws PasswordException, LockedException {
        String password;
        try {
            password = decryptPassword(uid, encryptedPassword);
        } catch (CryptographyException e) {
            throw new PasswordException(Error.DECRYPT_ERROR);
        }
        testConnection(password);
        preferencesHelper.setDBPassword(encryptedPassword);
    }

    public void setDBPassword(String uid, String encryptedPassword) throws PasswordException, LockedException {
        byte[] bytes = StringConverter.getBytesFromHexString(encryptedPassword);
        setDBPassword(uid, bytes);
    }

    public static String getPassword() {
        return password;
    }
}
