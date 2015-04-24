package md.varoinform.update;

import md.varoinform.Settings;
import md.varoinform.controller.cache.TagCache;
import md.varoinform.model.dao.DAOTag;
import md.varoinform.model.dao.DatabaseDao;
import md.varoinform.model.dao.EnterpriseDao;
import md.varoinform.model.search.FullTextSearcher;
import md.varoinform.model.utils.Configurator;
import md.varoinform.model.utils.SessionManager;
import md.varoinform.sequrity.PasswordManager;
import md.varoinform.sequrity.exception.UnregisteredDBExertion;
import md.varoinform.util.PreferencesHelper;
import md.varoinform.util.Request;
import md.varoinform.util.UrlCreator;
import md.varoinform.util.Zip;
import org.apache.commons.io.FileUtils;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 3/25/14
 * Time: 2:06 PM
 */
public class Updater {
    private final String uid;
    private final Path pathToDB;

    public Updater() throws UnregisteredDBExertion {
        this(getUID());
    }

    public Updater(String uid){
        this.uid = uid;
        pathToDB = Settings.pathToDB();
    }

    private static String getUID() throws UnregisteredDBExertion {
        PreferencesHelper preferencesHelper = new PreferencesHelper();
        String idDb = preferencesHelper.getUID();
        if (idDb == null) throw new UnregisteredDBExertion();
        return idDb;
    }

    public boolean checkUpdate() throws IOException, UnregisteredDBExertion, ExpiredException {
        UrlCreator creator = new UrlCreator(uid, "check");
        creator.setParam("version", Settings.getVersion());
        Request request = new Request(creator.getString());
        String content = request.timesGet(1);
        return parseContent(content);
    }

    private boolean parseContent(String content) throws IOException, ExpiredException, UnregisteredDBExertion {
        Pattern pattern = Pattern.compile("^value=(\\w+)$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS);
        if (content == null) throw new IOException();

        Matcher matcher = pattern.matcher(content);
        if (!matcher.matches()) throw new IOException();

        String message = matcher.group(1);
        switch (message.toLowerCase()) {
            case "yes":
                return true;
            case "no":
                return false;
            case "invalid_uid":
                throw new IOException(message);
            case "term_expired":
                throw new ExpiredException(message);
            case "unregistered_db":
                throw new UnregisteredDBExertion();
            default:
                throw new IOException(message);
        }
    }

    public Long update() throws IOException {
        File zipFile = download();
        File dbFile = getDBFile(Zip.unzip(zipFile));
        try {
            Files.delete(zipFile.toPath());
        } catch (Throwable ignored) {
        }

        Configuration cfg = getConfiguration(dbFile);

        Long updated = getUpdatedEnterprises(cfg);
        copyUserData(cfg);
        replaceDB(dbFile);

        confirm(5);

        FullTextSearcher.createIndex();

        return updated;
    }

    public File download() throws IOException {
        UrlCreator creator = new UrlCreator(uid);
        creator.setParam("version", Settings.getVersion());
        URL source = creator.getUrl();
        File destination = Paths.get(pathToDB.getParent().toString(), "Temp.zip").toFile();

        // if null_uid, unregistered_program, invalid_uid, term_expired or haven't updates
        // throw IOException
        FileUtils.copyURLToFile(source, destination);
        return destination;
    }

    private File getDBFile(Path tempFolderPath) throws IOException {
        File[] files = tempFolderPath.toFile().listFiles();
        if (files == null || files.length <= 0) throw new IOException("Has not db file");
        return files[0];
    }

    private Configuration getConfiguration(File dbFile) {
        String absPath = dbFile.getAbsolutePath();
        String pathToDb = absPath.substring(0, absPath.lastIndexOf(".h2.db"));
        return new Configurator(pathToDb, PasswordManager.getPassword()).configureWithoutIndex();
    }


    private Long getUpdatedEnterprises(Configuration cfg) {
        Date maxDate = EnterpriseDao.getMaxCheckDate();
        return EnterpriseDao.countWhereLastChangeGTE(cfg, maxDate);
    }

    private void copyUserData(Configuration cfg) {
        DAOTag.synchronizeWithDB(TagCache.instance.getTags(), cfg, false);
        DatabaseDao.setUID(DatabaseDao.getUID(), cfg);
        SessionManager.instance.shutdownAll();
    }

    private void replaceDB(File newDB) throws IOException {
        Path source = newDB.toPath();
        Path target = new File(String.format("%s.h2.db", pathToDB.toString())).toPath();
        Files.move(source, target, StandardCopyOption.ATOMIC_MOVE);
    }

    private void confirm(int tryCounter) throws IOException {
        UrlCreator creator = new UrlCreator(uid, "confirm");
        String url = creator.getString();
        Request request = new Request(url);
        request.timesGet(tryCounter);
    }
}
