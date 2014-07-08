package md.varoinform.update;

import md.varoinform.Settings;
import md.varoinform.model.Configurator;
import md.varoinform.model.entities.Database;
import md.varoinform.model.entities.Tag;
import md.varoinform.model.entities.TagEnterprise;
import md.varoinform.model.util.SessionManager;
import md.varoinform.model.util.Synchronizer;
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
import java.nio.file.StandardCopyOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 3/25/14
 * Time: 2:06 PM
 */
public class Updater {

    public static void main(String[] args) throws UnregisteredDBExertion, IOException, ExpiredException {
        Updater updater = new Updater();
        updater.update();
    }

    @SuppressWarnings("UnusedDeclaration")
    public boolean checkUpdate(String uid) throws IOException, UnregisteredDBExertion, ExpiredException {
        UrlCreator creator = new UrlCreator(uid, "check");
        Request request = new Request(creator.getString());
        String content = request.timesGet(1);
        System.out.println(content);
        return parseContent(content);
    }

    private boolean parseContent(String content) throws IOException, ExpiredException, UnregisteredDBExertion {
        Pattern pattern = Pattern.compile("^value=(\\w+)$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS);
        if (content == null) throw new IOException();

        Matcher matcher = pattern.matcher(content);
        if (!matcher.matches()) throw new IOException();

        String group = matcher.group(1);
        switch (group.toLowerCase()){
            case "yes" : return true;
            case "no" : return false;
            case "invalid_uid": throw new IOException(group);
            case "term_expired": throw new ExpiredException(group);
            case "unregistered_db": throw new UnregisteredDBExertion();
            default: throw new IOException(group);
        }
    }


    public void update() throws UnregisteredDBExertion, IOException {
        String uid = getUserId();

        UrlCreator creator = new UrlCreator(uid);
        URL source = creator.getUrl();
        File destination = getDBFile(getTempDB());

        // if null_uid, unregistered_program, invalid_uid, term_expired or haven't updates
        // throw IOException
        FileUtils.copyURLToFile(source, destination);

        copyUserData();
        replaceDB();
        confirm(uid, 5);
    }

    //ToDo: replace return for getUserId
    private String getUserId() throws UnregisteredDBExertion {
        PreferencesHelper preferencesHelper = new PreferencesHelper();
        String idDb = preferencesHelper.getUID();
        if (idDb == null) throw new UnregisteredDBExertion();
        return "0";
    }

    public String getTempDB() {
        return Settings.pathToDB().toString() + "Temp";
    }

    private File getDBFile(String name) {
        return new File(String.format("%s.h2.db", name));
    }


    private void copyUserData() {
        Session from = SessionManager.instance.getSession();
        Configuration cfg = new Configurator(getTempDB()).configure();
        Session to = SessionManager.instance.getSession("updating", cfg);

        for (Class c : new Class[] {Database.class, Tag.class, TagEnterprise.class}){
            Synchronizer.synchronize(c, from, to);
        }

        SessionManager.instance.shutdownAll();
    }

    private void replaceDB() throws IOException {
        Path source = getDBFile(getTempDB()).toPath();
        Path target = getDBFile(Settings.pathToDB().toString()).toPath();
        Files.move(source, target, StandardCopyOption.ATOMIC_MOVE);
    }


    private void confirm(String uid, int tryCounter) throws IOException {
        UrlCreator creator = new UrlCreator(uid, "confirm");
        String url = creator.getString();
        Request request = new Request(url);
        request.timesGet(tryCounter);
    }
}
