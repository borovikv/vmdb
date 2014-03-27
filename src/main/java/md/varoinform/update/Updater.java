package md.varoinform.update;

import md.varoinform.Settings;
import md.varoinform.model.Configurator;
import md.varoinform.model.entities.Database;
import md.varoinform.model.entities.Tag;
import md.varoinform.model.entities.TagEnterprise;
import md.varoinform.model.util.HibernateSessionFactory;
import md.varoinform.util.PreferencesHelper;
import org.apache.http.client.fluent.Request;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

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
            copyUserData();
            replaceDB();
        } catch (IOException e) {
            String eMessage = e.getMessage();
            System.out.println("error = " + eMessage);
        }
    }

    public String getUrl(boolean confirm) {
        String url = Settings.getUpdateUrl() + "?user=" + getUserId();
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

    private void copyUserData(){
        Configuration cfg = new Configurator(getNewDB()).configure();
        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();
        SessionFactory sessionFactory = cfg.buildSessionFactory(serviceRegistry);
        Session to = sessionFactory.openSession();

        synchronize(Database.class, HibernateSessionFactory.getSession(), to);
        synchronize(Tag.class, HibernateSessionFactory.getSession(), to);
        synchronize(TagEnterprise.class, HibernateSessionFactory.getSession(), to);
        System.out.println("copied");

    }

    public void synchronize(Class hibernateClass, Session from, Session to) throws HibernateException
    {
        Transaction trans = to.beginTransaction();
        List newData = from.createCriteria(hibernateClass).list();
        for (Object o : newData) {
            from.evict(o);
            to.replicate(o, ReplicationMode.OVERWRITE);
        }
        trans.commit();

    }

    private void replaceDB() {
        try {
            String target = Settings.pathToDB().toString();

            Files.move(Paths.get(getOutFile()), Paths.get(target + ".h2.db"), StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNewDB() {
        return "/home/drifter/DB/TempDB";
    }
}
