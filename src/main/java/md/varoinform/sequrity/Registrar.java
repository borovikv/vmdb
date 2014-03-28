package md.varoinform.sequrity;

import md.varoinform.Settings;
import md.varoinform.util.PreferencesHelper;
import md.varoinform.util.Request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/14/13
 * Time: 11:58 AM
 */
public class Registrar {

    PasswordManager passwordManager = new PasswordManager();

    public void registerByPhone(String idDB, String encryptedPassword) throws RegistrationException {
        setIdDB(idDB);
        setPassword(encryptedPassword);
    }

    private void setIdDB(String idDB) {
        PreferencesHelper preferencesHelper = new PreferencesHelper();
        preferencesHelper.setIdDb(idDB);
    }

    public void registerByInternet(String idDB) throws RegistrationException {
        String encryptedPassword = getPassword(idDB);
        setIdDB(idDB);
        setPassword(encryptedPassword);
    }

    private String getPassword(String idDB) throws RegistrationException {

        String url = Settings.getRegisterUrl() + "?code=" + getRegistrationCode(idDB);

        String response = request(url);

        Map<String, String> responseMap = parseResponse(response);
        String value = responseMap.get("value");
        if (responseMap.get("status").equals("ERROR")){
            throw new RegistrationException(value);
        }

        return value;
    }

    private void setPassword(String encryptedPassword) throws RegistrationException {
        try {
            byte[] bytes = StringUtils.getBytesFromHexString(encryptedPassword);
            passwordManager.setDBPassword(bytes);
        } catch (PasswordException e) {
            throw new RegistrationException(e);
        }
    }


    private Map<String, String> parseResponse(String response) throws RegistrationException {
        Map<String, String> result = new HashMap<>();
        String[] strings = response.replaceAll("[\r\n\t\\s]*", "").split("[=:]");

        // response must be a string with format STATUS:value
        if (strings.length != 2)  throw new RegistrationException(RegistrationException.RESPONSE_ERROR);

        result.put("status", strings[0]);
        result.put("value", strings[1]);
        return result;
    }

    private String request(String url) throws RegistrationException {
        Request request = new Request(url);
        try {
            return request.request();
        } catch (IOException e) {
            throw new RegistrationException(RegistrationException.CONNECTION_ERROR, e);
        }
    }

    //ToDo: getRegistrationCode (call from register by phone) - encrypt registration code
    public String getRegistrationCode(String idDB) {
        String idComp = MAC.instance.getMacAddressAsString();
        return idDB + idComp;
    }
}
