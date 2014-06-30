package md.varoinform.sequrity;

import md.varoinform.sequrity.exception.PasswordException;
import md.varoinform.sequrity.exception.PasswordNotExistException;
import md.varoinform.util.PreferencesHelper;


/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/12/13
 * Time: 5:14 PM
 */
public class PasswordManager {
    private final PreferencesHelper preferencesHelper = new PreferencesHelper();

    public String getDBPassword() throws PasswordNotExistException, PasswordException {
        Cypher cypher = new Cypher();
        byte[] key = getKey();
        String password;

        byte[] encryptedPassword = getEncryptedPassword();
        if (encryptedPassword == null) throw new PasswordNotExistException(PasswordNotExistException.PASSWORD_NOT_EXIST_EXCEPTION);

        password = cypher.decrypt(encryptedPassword, key);

        if( !testPassword(password)) throw new PasswordException(PasswordException.VALIDATION_ERROR);
        return password;
    }

    public byte[] getKey() {
        Cypher cypher = new Cypher() ;
        String keyString = getIdDb() + MAC.instance.getMacAddressAsString();
        return cypher.createKey(keyString);
    }


    //ToDo:real implementation testPassword (test connection to db)
    private boolean testPassword(String password) {
        return password.equals("secret");
    }

    private byte[] getEncryptedPassword() {
        return preferencesHelper.getDBPassword();
    }

    public void setDBPassword(byte[] encryptedPassword) throws PasswordException {
        preferencesHelper.setDBPassword(encryptedPassword);
        try {
            getDBPassword();
        } catch (PasswordNotExistException e) {
            e.printStackTrace();
        }
    }

    public void removeDBPassword() {
         preferencesHelper.removeDBPassword();
    }

    private String getIdDb() {
        return preferencesHelper.getIdDb();
    }
}
