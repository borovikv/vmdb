package md.varoinform.sequrity;

import md.varoinform.sequrity.exception.PasswordException;
import md.varoinform.sequrity.exception.PasswordNotExistException;
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

    public String getDBPassword(String uid) throws PasswordNotExistException, PasswordException {
        Cypher cypher = new Cypher();
        byte[] key = cypher.createKey(uid);
        String password;

        byte[] encryptedPassword = getEncryptedPassword();
        if (encryptedPassword == null) throw new PasswordNotExistException(PasswordNotExistException.PASSWORD_NOT_EXIST_EXCEPTION);

        password = cypher.decrypt(encryptedPassword, key);

        if( !testPassword(password)) throw new PasswordException(PasswordException.VALIDATION_ERROR);
        return password;
    }

    //ToDo:real implementation testPassword (test connection to db)
    private boolean testPassword(String password) {
        return password.equals("secret");
    }

    private byte[] getEncryptedPassword() {
        return preferencesHelper.getDBPassword();
    }

    public void setDBPassword(String uid, byte[] encryptedPassword) throws PasswordException {
        preferencesHelper.setDBPassword(encryptedPassword);
        try {
            getDBPassword(uid);
        } catch (PasswordNotExistException e) {
            e.printStackTrace();
        }
    }

    public void removeDBPassword() {
         preferencesHelper.removeDBPassword();
    }



    public void setDBPassword(String uid, String encryptedPassword) throws PasswordException {
        byte[] bytes = StringConverter.getBytesFromHexString(encryptedPassword);
        setDBPassword(uid, bytes);
    }
}
