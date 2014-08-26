package md.varoinform.sequrity;

import md.varoinform.sequrity.exception.CryptographyException;
import md.varoinform.util.PreferencesHelper;
import md.varoinform.util.StringConverter;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 7/22/14
 * Time: 9:52 AM
 */
@Ignore
public class TestCypher {
    @Test
    public void testEncrypt() throws CryptographyException {
        String uid = "TEST";
        String[] data = {"secret", "password"};
        for (String chunk : data) {
            byte[] bytes = encrypt(chunk, uid);
            assertNotNull(bytes);
            String hex = StringConverter.bytesToHex(bytes);
            System.out.println("testEncrypt " + chunk + " = " + hex + " length " + hex.length());
            String decrypt = decrypt(StringConverter.getBytesFromHexString(hex), uid);
            assertEquals(chunk, decrypt);
        }
    }

    private String getUID() {
        return new PreferencesHelper().getUID();
    }

    private byte[] encrypt(String data, String key) {
        Cypher cypher = new Cypher();
        try {
            return cypher.encrypt(data, cypher.createKey(key));
        } catch (CryptographyException e) {
            return null;
        }
    }


    @Test
    public void decryptPassword() throws CryptographyException {
        Cypher cypher = new Cypher();

        String database_id = "1111111111111111";
        String user_id = "000C299B664E";
        byte[] key = cypher.createKey(database_id + user_id);

        String encryptedPassword = "d8ec883f50cb260478538d3f9508d083";
        byte[] encryptedData = StringConverter.getBytesFromHexString(encryptedPassword);

        String password = cypher.decrypt(encryptedData, key);
        assertEquals("secret", password);
    }


    @Test
    public void testEncryptDecrypt() throws CryptographyException {
        String data = "password";
        String uid = getUID();

        byte[] encryptedKey = encrypt(data, uid);
        assertNotNull(encryptedKey);

        String decryptedData = decrypt(encryptedKey, uid);
        assertEquals(data, decryptedData);
    }


    private String decrypt(byte[] encryptedData, String key) throws CryptographyException {
        Cypher cypher = new Cypher();
        return cypher.decrypt(encryptedData, cypher.createKey(key));
    }

}
