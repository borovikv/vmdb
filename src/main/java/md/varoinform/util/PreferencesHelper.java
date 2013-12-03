package md.varoinform.util;

import md.varoinform.App;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class PreferencesHelper implements Serializable {
    private String columns = "columns";
    private final Preferences preferences;

    public PreferencesHelper() {
        preferences = Preferences.userNodeForPackage(App.class);
    }

    public String getUserFields() {
        String columns = preferences.get(this.columns, "default");
        if (columns.equals("default")) {
            ResourceBundle bundle = ResourceBundle.getBundle("VaroDB");
            columns = bundle.getString("defaultColumns");
        }
        return columns;
    }

    public void putPrefColumns(String value) {
        preferences.put(columns, value);
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
            delimiter = ";";
        }

        return buf.toString();
    }
}