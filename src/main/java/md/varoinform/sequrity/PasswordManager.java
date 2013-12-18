package md.varoinform.sequrity;

import md.varoinform.util.PreferencesHelper;

import java.security.Key;

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
        Key key = createKey(cypher);
        String password;

        byte[] encryptedPassword = getEncryptedPassword();
        if (encryptedPassword == null) throw new PasswordNotExistException(PasswordNotExistException.PASSWORD_NOT_EXIST_EXCEPTION);

        try {
            password = cypher.decrypt(encryptedPassword, key);
        } catch (CryptographyException e) {
            throw new PasswordException(PasswordException.DECRYPTION_ERROR, e);
        }

        if( !testPassword(password)) throw new PasswordException(PasswordException.VALIDATION_ERROR);
        return password;
    }

    public Key createKey(Cypher cypher) {
        byte[] idComp = MAC.instance.getMacAddress();
        byte[] idDB = getIdDb();
        return cypher.createKey(idComp, idDB);
    }

    //ToDo:real implementation
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

    //ToDo:real implementation
    private byte[] getIdDb() {
        String idDB = "0123456789123456";
        return StringUtils.getBytesFromHexString(idDB);
    }
}
