package md.varoinform.model;


import md.varoinform.Settings;
import md.varoinform.model.entities.*;
import md.varoinform.sequrity.PasswordManager;
import org.hibernate.cfg.Configuration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 3/26/14
 * Time: 3:51 PM
 */
public class Configurator {
    private static final Class[] entities = {
            Advertisement.class, AdvertisementText.class,
            Arc.class, Node.class, NodeTitle.class,
            Good.class, GoodTitle.class, GProduce.class,
            Brand.class,
            BusinessEntityType.class, BusinessEntityTypeTitle.class,
            Contact.class,
            ContactPerson.class,
            Email.class,
            Enterprise.class, EnterpriseTitle.class,
            Language.class,
            Person.class, PersonTitle.class,
            Phone.class,
            Position.class, PositionTitle.class,
            Region.class, RegionTitle.class,
            Sector.class, SectorTitle.class,
            Street.class, StreetTitle.class,
            Tag.class, TagEnterprise.class,
            Database.class,
            TopAdministrativeUnit.class, TopAdministrativeUnitTitle.class,
            Town.class, TownTitle.class,
            Url.class
    };
    private final String pathToDb;
    private final String password;

    public Configurator() {
        pathToDb = Settings.pathToDB().toString();
        password = PasswordManager.getPassword();
    }

    public Configurator(String pathToDb) {
        if (pathToDb == null || pathToDb.isEmpty()) throw new IllegalArgumentException();
        Path path = Paths.get(pathToDb);
        if (Files.notExists(path.getParent()))
            throw new RuntimeException("file " + path.toAbsolutePath().toString() + " not found");
        this.pathToDb = path.toString();
        password = PasswordManager.getPassword();
    }

    public Configuration configure() {
        Configuration cfg = getConfiguration();
        setIndex(cfg);
        showSql(cfg, false);
        //setAuto(cfg, "update");
        return cfg;
    }

    public Configuration configureWithoutIndex(){
        Configuration cfg = getConfiguration();
        showSql(cfg, false);
        cfg.setProperty("hibernate.search.autoregister_listeners", "false");
        return cfg;
    }

    public Configuration getConfiguration() {
        Configuration cfg = new Configuration();

        for (Class entity : entities) {
            cfg.addAnnotatedClass(entity);
        }

        cfg.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");

        cfg.setProperty("hibernate.connection.url", "jdbc:h2:file:" + pathToDb);
        cfg.setProperty("hibernate.connection.username", "admin");
        cfg.setProperty("hibernate.connection.password", password);

        cfg.setProperty("hibernate.connection.pool_size", "1");
        cfg.setProperty("hibernate.current_session_context_class", "thread");

        cfg.setProperty("hibernate.generate_statistics", "false");
        cfg.setProperty("hibernate.use_sql_comments", "false");
        cfg.setProperty("hibernate.connection.autocommit", "false");

        return cfg;
    }

    public void setIndex(Configuration cfg) {
        cfg.setProperty("hibernate.search.default.directory_provider", "filesystem");
        cfg.setProperty("hibernate.search.default.indexBase", pathToDb + "/indexes");
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setAuto(Configuration cfg, String value) {
        cfg.setProperty("hibernate.hbm2ddl.auto", value);
    }

    public void showSql(Configuration cfg, boolean b) {
        cfg.setProperty("hibernate.show_sql", Boolean.toString(b));

    }

}
