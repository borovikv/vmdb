package md.varoinform.sequrity;

import md.varoinform.sequrity.exception.CryptographyException;
import md.varoinform.sequrity.exception.PasswordException;
import md.varoinform.sequrity.exception.PasswordNotExistException;
import md.varoinform.util.PreferencesHelper;
import md.varoinform.util.StringConverter;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/12/13
 * Time: 5:00 PM
 */
public class TestGetKey {

    @Test
    public void testEncryptDecrypt(){
        String key = "password";
        byte[] encryptedKey = encrypt(key);
        System.out.println("encrypted key = " + encryptedKey.length);
        assertNotNull(encryptedKey);
        String decryptedKey = decrypt(encryptedKey);
        assertEquals(key, decryptedKey);
    }

    private byte[] encrypt(String aKey) {
        Cypher cypher = new Cypher();
        try {
            return cypher.encrypt(aKey, getKey());
        } catch (CryptographyException e) {
            return null;
        }
    }

    private String decrypt(byte[] encryptedKey) {
        Cypher cypher = new Cypher();
        return cypher.decrypt(encryptedKey, getKey());
    }

    private byte[] getKey() {
        Cypher cypher = new Cypher();
        return cypher.createKey(getUID());
    }

    private String getUID() {
        return new PreferencesHelper().getUID();
    }


    @Test
    public void testPassGetKey() throws PasswordNotExistException, PasswordException {
        String aKey = "secret";

        PasswordManager passwordManager = new PasswordManager();
        byte[] encryptedKey = encrypt(aKey);
        assertNotNull(encryptedKey);
        passwordManager.setDBPassword(getUID(), encryptedKey);

        String key = passwordManager.getDBPassword(getUID());
        assertEquals(key, aKey);
    }


    @Test(expected = PasswordNotExistException.class)
    public void testNotExistKey() throws PasswordNotExistException, PasswordException {
        PasswordManager passwordManager = new PasswordManager();
        passwordManager.getDBPassword(getUID());
    }


    @Test(expected = PasswordException.class)
    public void testFailKey() throws PasswordNotExistException, PasswordException {
        PasswordManager passwordManager = new PasswordManager();
        byte[] encryptedKey = encrypt("password");
        assertNotNull(encryptedKey);
        passwordManager.setDBPassword(getUID(), encryptedKey);
        passwordManager.getDBPassword(null);
    }

    @Test
    public void testEncrypt(){
        String password = "secret";
        Cypher cypher = new Cypher();
        byte[] bytes = new byte[0];
        try {
            bytes = cypher.encrypt(password, getKey());
        } catch (CryptographyException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        System.out.println(StringConverter.bytesToHex(bytes));
    }

    @After
    public void after() {
        PasswordManager passwordManager = new PasswordManager();
        passwordManager.removeDBPassword();
    }

    @Test
    public void decryptPassword(){
        Cypher cypher = new Cypher();
        String encryptedPassword = "d8ec883f50cb260478538d3f9508d083";
        String database_id = "1111111111111111";
        String user_id = "000C299B664E";
        byte[] key = cypher.createKey(database_id + user_id);
        byte[] encryptedData = StringConverter.getBytesFromHexString(encryptedPassword);
        String password = cypher.decrypt(encryptedData, key);
        assertEquals("secret", password);
    }

}
