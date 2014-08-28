package md.varoinform.sequrity;

import md.varoinform.Settings;
import md.varoinform.model.dao.DatabaseDao;
import md.varoinform.sequrity.exception.Error;
import md.varoinform.sequrity.exception.LockedException;
import md.varoinform.sequrity.exception.PasswordException;
import md.varoinform.sequrity.exception.RegistrationException;
import md.varoinform.util.PreferencesHelper;
import md.varoinform.util.Request;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/14/13
 * Time: 11:58 AM
 */
public class Registrar {

    PasswordManager passwordManager = new PasswordManager();

    public void register(String uid, String encryptedPassword) throws RegistrationException, PasswordException, LockedException {
        passwordManager.setDBPassword(uid, encryptedPassword);
        setUID(uid);
    }

    private void setUID(String uid) {
        PreferencesHelper preferencesHelper = new PreferencesHelper();
        preferencesHelper.setUID(uid);
        DatabaseDao.clear();
        DatabaseDao.setUID(uid);
    }

    public void registerByInternet(String uid) throws RegistrationException, PasswordException, LockedException {
        String encryptedPassword = getPassword(uid);
        register(uid, encryptedPassword);
    }

    private String getPassword(String idDB) throws RegistrationException {
        String url = String.format("%s?id=%s", Settings.getRegisterUrl(), idDB);
        String response = request(url);
        return parseResponse(response);
    }

    private String request(String url) throws RegistrationException {
        Request request = new Request(url);
        try {
            return request.timesGet(1);
        } catch (IOException e) {
            throw new RegistrationException(md.varoinform.sequrity.exception.Error.CONNECTION_ERROR, e);
        }
    }

    private String parseResponse(String response) throws RegistrationException {
        String[] strings = response.replaceAll("[\r\n\t\\s]*", "").split("[=:]");

        // response must be a string with format STATUS:value
        if (strings.length != 2)  throw new RegistrationException(Error.RESPONSE_ERROR);
        if (strings[0].equalsIgnoreCase("ERROR")) {
            throw new RegistrationException(Error.parseError(strings[1]));
        }

        return strings[1];
    }
}
