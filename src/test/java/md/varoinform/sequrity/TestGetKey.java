package md.varoinform.sequrity;

import md.varoinform.model.util.SessionManager;
import md.varoinform.sequrity.exception.CryptographyException;
import md.varoinform.sequrity.exception.Error;
import md.varoinform.sequrity.exception.PasswordException;
import md.varoinform.util.PreferencesHelper;
import md.varoinform.util.StringConverter;
import org.junit.After;
import org.junit.Before;
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
    public void testPassGetKey() throws PasswordException {
        String aKey = "password";

        PasswordManager passwordManager = new PasswordManager();
        byte[] encryptedKey = encrypt(aKey);
        assertNotNull(encryptedKey);
        new PreferencesHelper().removeDBPassword();
        System.out.println("password " + StringConverter.bytesToHex(encryptedKey));
        passwordManager.setDBPassword(getUID(), encryptedKey);
        String key = passwordManager.getDBPassword(getUID());

        assertEquals(key, aKey);
    }


    @Test
    public void testNotExistKey(){
        PasswordManager passwordManager = new PasswordManager();
        md.varoinform.sequrity.exception.Error type = null;
        try {
            passwordManager.getDBPassword(getUID());
        } catch (PasswordException e) {
            type = e.getType();
        }
        assertEquals(type, Error.PASSWORD_NOT_EXIST_ERROR);
    }


    @Test(expected = PasswordException.class)
    public void testFailKey() throws PasswordException {
        PasswordManager passwordManager = new PasswordManager();
        byte[] encryptedKey = encrypt("secret");
        assertNotNull(encryptedKey);
        passwordManager.setDBPassword(getUID(), encryptedKey);
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

    @Before
    public void before() {
        new PreferencesHelper().removeDBPassword();
    }
    @After
    public void after() {
        SessionManager.instance.shutdownAll();
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
