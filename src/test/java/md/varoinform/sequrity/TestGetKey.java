package md.varoinform.sequrity;

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
            return cypher.encrypt(aKey, new PasswordManager().getKey());
        } catch (CryptographyException e) {
            return null;
        }
    }

    private String decrypt(byte[] encryptedKey) {
        Cypher cypher = new Cypher();
        return cypher.decrypt(encryptedKey, new PasswordManager().getKey());
    }


    @Test
    public void testPassGetKey() throws PasswordNotExistException, PasswordException {
        String aKey = "secret";

        PasswordManager passwordManager = new PasswordManager();
        byte[] encryptedKey = encrypt(aKey);
        assertNotNull(encryptedKey);
        passwordManager.setDBPassword(encryptedKey);

        String key = passwordManager.getDBPassword();
        assertEquals(key, aKey);
    }


    @Test(expected = PasswordNotExistException.class)
    public void testNotExistKey() throws PasswordNotExistException, PasswordException {
        PasswordManager passwordManager = new PasswordManager();
        passwordManager.getDBPassword();
    }


    @Test(expected = PasswordException.class)
    public void testFailKey() throws PasswordNotExistException, PasswordException {
        PasswordManager passwordManager = new PasswordManager();
        byte[] encryptedKey = encrypt("password");
        assertNotNull(encryptedKey);
        passwordManager.setDBPassword(encryptedKey);
        passwordManager.getDBPassword();
    }

    @Test
    public void testEncrypt(){
        String password = "secret";
        Cypher cypher = new Cypher();
        byte[] bytes = new byte[0];
        try {
            bytes = cypher.encrypt(password, new PasswordManager().getKey());
        } catch (CryptographyException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        System.out.println(StringUtils.bytesToHex(bytes));
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
        byte[] encryptedData = StringUtils.getBytesFromHexString(encryptedPassword);
        String password = cypher.decrypt(encryptedData, key);
        assertEquals("secret", password);
    }

}
