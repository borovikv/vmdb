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
    private final String pathToDb;
    private final String password = PasswordManager.getPassword() + "";

    public Configurator() {
        pathToDb = Settings.pathToDB().toString();
    }

    public Configurator(String pathToDb) {
        if (pathToDb == null || pathToDb.isEmpty()) throw new IllegalArgumentException();
        Path path = Paths.get(pathToDb);
        if (Files.notExists(path.getParent())) throw new RuntimeException("file " + path.toAbsolutePath().toString() + " not found");
        this.pathToDb = path.toString();
    }

    public Configuration configure() {
        System.out.println(password);
        Configuration cfg = getConfiguration();
        showSql(cfg, false);
        //setAuto(cfg, "update");
        return cfg;
    }

    public Configuration getConfiguration() {
        Configuration cfg  = new Configuration();
        cfg.addAnnotatedClass(Advertisement.class);
        cfg.addAnnotatedClass(AdvertisementText.class);
        cfg.addAnnotatedClass(Arc.class);
        cfg.addAnnotatedClass(Node.class);
        cfg.addAnnotatedClass(NodeTitle.class);

        cfg.addAnnotatedClass(Good.class);
        cfg.addAnnotatedClass(GoodTitle.class);
        cfg.addAnnotatedClass(GProduce.class);

        cfg.addAnnotatedClass(Brand.class);
        cfg.addAnnotatedClass(BusinessEntityType.class);
        cfg.addAnnotatedClass(BusinessEntityTypeTitle.class);
        cfg.addAnnotatedClass(Contact.class);
        cfg.addAnnotatedClass(ContactPerson.class);
        cfg.addAnnotatedClass(Email.class);
        cfg.addAnnotatedClass(Enterprise.class);
        cfg.addAnnotatedClass(EnterpriseTitle.class);
        cfg.addAnnotatedClass(Language.class);
        cfg.addAnnotatedClass(Person.class);
        cfg.addAnnotatedClass(PersonTitle.class);
        cfg.addAnnotatedClass(Phone.class);
        cfg.addAnnotatedClass(Position.class);
        cfg.addAnnotatedClass(PositionTitle.class);
        cfg.addAnnotatedClass(Region.class);
        cfg.addAnnotatedClass(RegionTitle.class);
        cfg.addAnnotatedClass(Sector.class);
        cfg.addAnnotatedClass(SectorTitle.class);
        cfg.addAnnotatedClass(Street.class);
        cfg.addAnnotatedClass(StreetTitle.class);
        cfg.addAnnotatedClass(Tag.class);
        cfg.addAnnotatedClass(TagEnterprise.class);
        cfg.addAnnotatedClass(Database.class);
        cfg.addAnnotatedClass(TopAdministrativeUnit.class);
        cfg.addAnnotatedClass(TopAdministrativeUnitTitle.class);
        cfg.addAnnotatedClass(Town.class);
        cfg.addAnnotatedClass(TownTitle.class);
        cfg.addAnnotatedClass(Url.class);

        cfg.setProperty("hibernate.connection.driver_class", "org.h2.Driver");

        cfg.setProperty("hibernate.connection.url", "jdbc:h2:file:" + pathToDb);
        cfg.setProperty("hibernate.connection.username", "admin");
        cfg.setProperty("hibernate.connection.password", password);
        cfg.setProperty("hibernate.connection.pool_size", "10");
        cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        cfg.setProperty("hibernate.generate_statistics", "false");
        cfg.setProperty("hibernate.use_sql_comments", "false");
        cfg.setProperty("hibernate.connection.autocommit", "false");
        cfg.setProperty("hibernate.current_session_context_class", "thread");

        cfg.setProperty("hibernate.search.default.directory_provider", "filesystem");
        cfg.setProperty("hibernate.search.default.indexBase", pathToDb + "/indexes");
        return cfg;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setAuto(Configuration cfg, String value){
        cfg.setProperty("hibernate.hbm2ddl.auto", value);
    }

    public void showSql(Configuration cfg, boolean b){
        cfg.setProperty("hibernate.show_sql", Boolean.toString(b));

    }

}
