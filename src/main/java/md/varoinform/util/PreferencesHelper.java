package md.varoinform.util;

import md.varoinform.App;

import java.io.Serializable;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class PreferencesHelper implements Serializable {
    private String columns = "columns";
    private final Preferences preferences;

    public PreferencesHelper() {
        preferences = Preferences.userNodeForPackage(App.class);
    }

    public String getPrefColumns() {
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
}