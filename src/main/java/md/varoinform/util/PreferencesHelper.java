package md.varoinform.util;

import md.varoinform.App;
import md.varoinform.Settings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.prefs.Preferences;

public class PreferencesHelper implements Serializable {
    private static final String SHOW_TEXT_IN_BUTTONS = "show_text_in_buttons";
    private static final String IS_INDEXED = "is_indexed";
    private static final String USE_PROXY = "use_proxy";
    private static final String LANGUAGE_KEY = "language";
    private static final String PROXY_ADDRESS = "proxy_address";
    private static final String PROXY_PORT = "proxy_port";
    private static final String PROXY_USER = "proxy_finder";
    private static final String PROXY_PASSWORD = "proxy_password";
    private static List<String> userFields;
    private final String fieldKey = "columns";
    private final String defaultDelimiter = ";";
    private final Preferences preferences;
    private String passwordKey = "password";
    private String uidKey = "idDB";

    public PreferencesHelper() {
        preferences = Preferences.userNodeForPackage(App.class);
    }

    public List<String> getUserFields() {
        if (userFields != null) return userFields;

        String columns = preferences.get(fieldKey, "default");

        if (columns.equals("default")) {
            return getDefaultFields();
        }

        userFields = toList(columns);
        return userFields;
    }

    private List<String> toList(String columns) {
        List<String> result =  new ArrayList<>();
        Collections.addAll(result, columns.split(defaultDelimiter));
        return result;
    }

    public List<String> getDefaultFields() {
        return toList(Settings.getDefaultColumns());
    }

    public void putUserFields(List<String> colNames){
        String value = concatList(colNames);
        userFields = colNames;
        putUserFields(value);
    }

    private String concatList(List<String> names) {
        StringBuilder buf = new StringBuilder();
        String delimiter = "";
        for (String name : names) {
            buf.append(delimiter);
            buf.append(name);
            delimiter = defaultDelimiter;
        }

        return buf.toString();
    }

    private void putUserFields(String value) {
        preferences.put(fieldKey, value);
    }

    public byte[] getDBPassword() {
        return preferences.getByteArray(passwordKey, null);
    }

    //------------------------------------------------------------------------------------------------------------------
    public void setDBPassword(byte[] encryptedPassword) {
        preferences.putByteArray(passwordKey, encryptedPassword);
    }

    public void removeDBPassword() {
        remove(passwordKey);
    }

    public String getUID(){
        return preferences.get(uidKey, null);
    }

    //------------------------------------------------------------------------------------------------------------------
    public void setUID(String uid) {
        preferences.put(uidKey, uid);
    }

    //------------------------------------------------------------------------------------------------------------------
    public void remove(String key) {
        preferences.remove(key);
    }
    //------------------------------------------------------------------------------------------------------------------

    public Integer getCurrentLanguage(){
        return preferences.getInt(LANGUAGE_KEY, 1);
    }

    public void setCurrentLanguage(Integer id){
        preferences.putInt(LANGUAGE_KEY, id);
    }

    public boolean getShowTextInButton(){
        return preferences.getBoolean(SHOW_TEXT_IN_BUTTONS, true);
    }

    //------------------------------------------------------------------------------------------------------------------
    public void setShowTextInButton(boolean showTextInButton) {
        preferences.putBoolean(SHOW_TEXT_IN_BUTTONS, showTextInButton);
    }

    public boolean getIsIndexed() {
        return preferences.getBoolean(IS_INDEXED, false);
    }

    public void setIsIndexed(boolean indexed){
        preferences.putBoolean(IS_INDEXED, indexed);
    }

    public boolean getUseProxy() {
        return preferences.getBoolean(USE_PROXY, false);
    }

    public void setUseProxy(boolean b) {
        preferences.putBoolean(USE_PROXY, b);
    }


    public void setProxyAddress(String address) {
        preferences.put(PROXY_ADDRESS, address);
    }

    public void setProxyPort(String port) {
        int value = -1;
        if (port != null){
            try {
                value = Integer.parseInt(port);
            } catch (RuntimeException ignored){

            }
        }
        preferences.putInt(PROXY_PORT, value);
    }

    public void setProxyUser(String user) {
        preferences.put(PROXY_USER, user);
    }

    public void setProxyPassword(String password) {
        preferences.put(PROXY_PASSWORD, password);
    }

    public String getProxyUser() {
        return preferences.get(PROXY_USER, "");
    }

    public Integer getProxyPort() {
        int value = preferences.getInt(PROXY_PORT, -1);
        return value >= 0 ? value : null;
    }

    public String getProxyAddress() {
        return preferences.get(PROXY_ADDRESS, "");
    }

    public String getProxyPassword() {
        return preferences.get(PROXY_PASSWORD, "");
    }
}