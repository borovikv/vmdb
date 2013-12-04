package md.varoinform.util;

import md.varoinform.App;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class PreferencesHelper implements Serializable {
    private final String columns = "columns";
    private final Preferences preferences;
    private final String defaultDelimiter = ";";

    public PreferencesHelper() {
        preferences = Preferences.userNodeForPackage(App.class);
    }

    public List<String> getUserFields() {
        String columns = preferences.get(this.columns, "default");

        if (columns.equals("default")) {
            columns = getDefaultColumns();
        }

        List<String> result =  new ArrayList<>();
        Collections.addAll(result, columns.split(defaultDelimiter));
        return result;
    }

    private String getDefaultColumns() {
        ResourceBundle bundle = ResourceBundle.getBundle("VaroDB");
        return bundle.getString("defaultColumns");
    }

    public void putPrefColumns(List<String> colNames){
        String value = concatList(colNames);
        putPrefColumns(value);
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

    private void putPrefColumns(String value) {
        preferences.put(columns, value);
    }

}