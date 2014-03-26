package md.varoinform.update;

import md.varoinform.util.PreferencesHelper;
import org.apache.http.client.fluent.Request;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 3/25/14
 * Time: 2:06 PM
 */
public class Updater {
    public static void main(String[] args) {
        new Updater().update();
    }

    public void update(){
        try {
            org.apache.commons.io.FileUtils.copyURLToFile(new URL(getUrl(false)), new File(getOutFile()));
            confirm(5);
            setDbID();
        } catch (IOException e) {
            String eMessage = e.getMessage();
            System.out.println(eMessage);
        }
    }

    public String getUrl(boolean confirm) {
        String url = ResourceBundle.getBundle("VaroDB").getString("update_url");
        url += "?user=" + getUserId();
        if (confirm){
            url += "&confirm=true";
        }
        System.out.println(url);
        return url;
    }

    @SuppressWarnings("UnusedDeclaration")
    private String getUserId() {
        PreferencesHelper preferencesHelper = new PreferencesHelper();
        String idDb = preferencesHelper.getIdDb();
        return "0";
    }

    private String getOutFile() {
        return "/home/drifter/DB/TempDB.h2.db";
    }

    private void confirm(int tryCounter){
        if (tryCounter <= 0) return;
        try {
            Request.Get(getUrl(true)).execute();
        } catch (IOException e) {
            e.printStackTrace();
            confirm(tryCounter - 1);
        }
    }

    //ToDo: Updater.SetDbId() set db ID
    private void setDbID(){

    }
}
