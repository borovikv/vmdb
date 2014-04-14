package md.varoinform.util;

import md.varoinform.App;
import md.varoinform.Settings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.prefs.Preferences;

public class PreferencesHelper implements Serializable {
    private static final String DIVIDE_KEY = "divide";
    private final String fieldKey = "columns";
    private final Preferences preferences;
    private final String defaultDelimiter = ";";
    private String passwordKey = "password";
    private String idDbKey = "idDB";
    private static final String LANGUAGE_KEY = "language";
    private List<String> userFields;

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

    //------------------------------------------------------------------------------------------------------------------
    public void setDBPassword(byte[] encryptedPassword) {
        preferences.putByteArray(passwordKey, encryptedPassword);
    }

    public byte[] getDBPassword() {
        return preferences.getByteArray(passwordKey, null);
    }

    public void removeDBPassword() {
        remove(passwordKey);
    }

    //------------------------------------------------------------------------------------------------------------------
    public void setIdDb(String idDb) {
        preferences.put(idDbKey, idDb);
    }

    @SuppressWarnings("UnusedDeclaration")
    public String getIdDb(){
        return preferences.get(idDbKey, null);
    }

    //------------------------------------------------------------------------------------------------------------------
    public void remove(String key) {
        preferences.remove(key);
    }
    //------------------------------------------------------------------------------------------------------------------

    public void setCurrentLanguage(String title){
        preferences.put(LANGUAGE_KEY, title);
    }

    public String getCurrentLanguage(){
        return preferences.get(LANGUAGE_KEY, "");
    }

    //------------------------------------------------------------------------------------------------------------------

    @SuppressWarnings("UnusedDeclaration")
    public void setDivideLocation(Double value){
        preferences.putDouble(DIVIDE_KEY, value);
    }

    public double getDivideLocation(){
        return preferences.getDouble(DIVIDE_KEY, .65d);
    }
}