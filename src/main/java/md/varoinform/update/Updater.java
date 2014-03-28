package md.varoinform.update;

import md.varoinform.Settings;
import md.varoinform.model.Configurator;
import md.varoinform.model.entities.Database;
import md.varoinform.model.entities.Tag;
import md.varoinform.model.entities.TagEnterprise;
import md.varoinform.model.util.SessionManager;
import md.varoinform.model.util.Synchronizer;
import md.varoinform.sequrity.UnregisteredDBExertion;
import md.varoinform.util.PreferencesHelper;
import md.varoinform.util.UrlCreator;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.fluent.Response;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.regex.Pattern;


/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 3/25/14
 * Time: 2:06 PM
 */
public class Updater {

    public static void main(String[] args) throws UnregisteredDBExertion {
        new Updater().update();
    }

    public boolean checkUpdate() throws UnregisteredDBExertion {
        UrlCreator creator = new UrlCreator(getUserId());
        creator.setCheck(true);
        String url = creator.getString();

        String content = requestGetExecuteTimes(url, 1);
        return parseContent(content);
    }

    private String requestGetExecuteTimes(String url, int times) throws UnregisteredDBExertion {
        if (times <= 0) return null;
        try {
            Response execute = Request.Get(url).execute();
            HttpResponse response = execute.returnResponse();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            return out.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return requestGetExecuteTimes(url, times - 1);
        }
    }

    private boolean parseContent(String content) {
        Pattern pattern = Pattern.compile("^value=(yes|true)$", Pattern.CASE_INSENSITIVE);
        return content != null && pattern.matcher(content).matches();
    }

    public void update() throws UnregisteredDBExertion {
        try {
            UrlCreator creator = new UrlCreator(getUserId());
            URL source = creator.getUrl();
            File destination = new File(getDBFile(getTempDB()));
            FileUtils.copyURLToFile(source, destination);
            copyUserData();
            replaceDB();
            confirm(5);
        } catch (IOException e) {
            String eMessage = e.getMessage();
            System.out.println("error = " + eMessage);
        }
    }


    private String getUserId() throws UnregisteredDBExertion {
        PreferencesHelper preferencesHelper = new PreferencesHelper();
        String idDb = preferencesHelper.getIdDb();
        if (idDb == null) throw new UnregisteredDBExertion();
        return "0";
    }

    private String getDBFile(String name) {
        return name + ".h2.db";
    }

    public String getTempDB() {
        return Settings.pathToDB().toString() + "Temp";
    }

    private void confirm(int tryCounter) throws UnregisteredDBExertion {
        UrlCreator creator = new UrlCreator(getUserId());
        creator.setConfirm(true);
        String url = creator.getString();

        requestGetExecuteTimes(url, tryCounter);
    }

    private void copyUserData(){
        Session from = SessionManager.getSession();
        Configuration cfg = new Configurator(getTempDB()).configure();
        Session to = SessionManager.getSession("updating", cfg);

        for (Class c : new Class[] {Database.class, Tag.class, TagEnterprise.class}){
            Synchronizer.synchronize(c, from, to);

        }

        SessionManager.shutdownAll();
    }

    private void replaceDB() {
        try {
            Path source = Paths.get(getDBFile(getTempDB()));
            Path target = Paths.get(getDBFile(Settings.pathToDB().toString()));

            Files.move(source, target, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
