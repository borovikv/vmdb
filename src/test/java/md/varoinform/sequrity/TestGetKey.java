package md.varoinform.sequrity;

import md.varoinform.model.util.SessionManager;
import md.varoinform.sequrity.exception.CryptographyException;
import md.varoinform.sequrity.exception.PasswordException;
import md.varoinform.sequrity.exception.UnregisteredDBExertion;
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
    public void testPassGetKey() throws PasswordException, UnregisteredDBExertion {
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

    private byte[] encrypt(String aKey) {
        Cypher cypher = new Cypher();
        try {
            return cypher.encrypt(aKey, cypher.createKey(getUID()));
        } catch (CryptographyException e) {
            return null;
        }
    }

    private String getUID() {
        return new PreferencesHelper().getUID();
    }


    @Test(expected = UnregisteredDBExertion.class)
    public void testNotExistKey() throws UnregisteredDBExertion, PasswordException {
        PasswordManager passwordManager = new PasswordManager();
        passwordManager.getDBPassword(getUID());
    }


    @Test(expected = PasswordException.class)
    public void testFailKey() throws PasswordException {
        PasswordManager passwordManager = new PasswordManager();
        byte[] encryptedKey = encrypt("secret");
        assertNotNull(encryptedKey);
        passwordManager.setDBPassword(getUID(), encryptedKey);
    }



    @Before
    public void before() {
        new PreferencesHelper().removeDBPassword();
    }
    @After
    public void after() {
        SessionManager.instance.shutdownAll();
    }

}
