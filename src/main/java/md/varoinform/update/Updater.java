package md.varoinform.update;

import md.varoinform.Settings;
import md.varoinform.model.Configurator;
import md.varoinform.model.entities.Database;
import md.varoinform.model.entities.Tag;
import md.varoinform.model.entities.TagEnterprise;
import md.varoinform.model.util.SessionManager;
import md.varoinform.model.util.Synchronizer;
import md.varoinform.sequrity.exception.PasswordException;
import md.varoinform.sequrity.exception.UnregisteredDBExertion;
import md.varoinform.util.PreferencesHelper;
import md.varoinform.util.Request;
import md.varoinform.util.UrlCreator;
import org.apache.commons.io.FileUtils;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

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

    public static void main(String[] args) throws UnregisteredDBExertion, IOException {
        new Updater().update();
    }

    public boolean checkUpdate() throws UnregisteredDBExertion, IOException {
        UrlCreator creator = new UrlCreator(getUserId());
        creator.setParam("check", "true");
        String url = creator.getString();
        Request request = new Request(url);
        String content = request.timesGet(1);
        System.out.println(content);
        return parseContent(content);
    }

    private boolean parseContent(String content) {
        Pattern pattern = Pattern.compile("^value=(yes|true)$", Pattern.CASE_INSENSITIVE);
        return content != null && pattern.matcher(content).matches();
    }

    public void update() throws UnregisteredDBExertion, IOException {
        UrlCreator creator = new UrlCreator(getUserId());
        URL source = creator.getUrl();
        File destination = new File(getDBFile(getTempDB()));
        FileUtils.copyURLToFile(source, destination);
        try {
            copyUserData();
            replaceDB();
            confirm(5);
        } catch (PasswordException e) {
            e.printStackTrace();
            //TODO: handle Password exception in update
        }
    }

    //ToDo: replace return for getUserId
    private String getUserId() throws UnregisteredDBExertion {
        PreferencesHelper preferencesHelper = new PreferencesHelper();
        String idDb = preferencesHelper.getUID();
        if (idDb == null) throw new UnregisteredDBExertion();
        return "0";
    }

    private String getDBFile(String name) {
        return name + ".h2.db";
    }

    public String getTempDB() {
        return Settings.pathToDB().toString() + "Temp";
    }

    private void confirm(int tryCounter) throws UnregisteredDBExertion, IOException {
        UrlCreator creator = new UrlCreator(getUserId());
        creator.setParam("confirm", "true");
        String url = creator.getString();
        Request request = new Request(url);
        request.timesGet(tryCounter);
    }

    private void copyUserData() throws PasswordException {
        Session from = SessionManager.instance.getSession();
        Configuration cfg = new Configurator(getTempDB()).configure();
        Session to = SessionManager.instance.getSession("updating", cfg);

        for (Class c : new Class[] {Database.class, Tag.class, TagEnterprise.class}){
            Synchronizer.synchronize(c, from, to);

        }

        SessionManager.instance.shutdownAll();
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
