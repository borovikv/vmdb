package md.varoinform.sequrity;

import md.varoinform.Settings;
import md.varoinform.model.dao.TransactionDaoHibernateImpl;
import md.varoinform.model.entities.Database;
import md.varoinform.sequrity.exception.*;
import md.varoinform.sequrity.exception.Error;
import md.varoinform.util.PreferencesHelper;
import md.varoinform.util.Request;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/14/13
 * Time: 11:58 AM
 */
public class Registrar {

    PasswordManager passwordManager = new PasswordManager();

    public void registerByPhone(String uid, String encryptedPassword) throws RegistrationException, PasswordException {
        passwordManager.setDBPassword(uid, encryptedPassword);
        setUID(uid);
    }

    private void setUID(String uid) {
        PreferencesHelper preferencesHelper = new PreferencesHelper();
        preferencesHelper.setUID(uid);
        Database record = new Database();
        record.setUid(uid);
        TransactionDaoHibernateImpl<Database, Long> dao = new TransactionDaoHibernateImpl<>(Database.class);
        List<Database> all = dao.getAll();
        if (all.size() > 0){
            for (Database database : all) {
                dao.delete(database);
            }
        }
        dao.save(record);
    }

    public void registerByInternet(String uid) throws RegistrationException, PasswordException {
        String encryptedPassword = getPassword(uid);
        passwordManager.setDBPassword(uid, encryptedPassword);
        setUID(uid);
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
