package md.varoinform.model;


import md.varoinform.Settings;
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

    public Configurator() {
        Path path = Paths.get(Settings.getWorkFolder(), "database", "DB");
        if (Files.notExists(path.getParent())) throw new RuntimeException("file " + path.toAbsolutePath().toString() + " not found");
        this.pathToDb = path.toString();
    }

    public Configurator(String pathToDb) {
        if (pathToDb == null || pathToDb.isEmpty()) throw new IllegalArgumentException();
        Path path = Paths.get(pathToDb);
        if (Files.notExists(path.getParent())) throw new RuntimeException("file " + path.toAbsolutePath().toString() + " not found");
        this.pathToDb = path.toString();
    }


    public Configuration configure(){
        Configuration cfg = getConfiguration();
        setAuto(cfg, "update");
        showSql(cfg, false);
        return cfg;
    }

    public Configuration getConfiguration() {
        Configuration cfg  = new Configuration();
        cfg.addAnnotatedClass(md.varoinform.model.entities.Advertisement.class);
        cfg.addAnnotatedClass(md.varoinform.model.entities.AdvertisementText.class);
        cfg.addAnnotatedClass(md.varoinform.model.entities.Branch.class);
        cfg.addAnnotatedClass(md.varoinform.model.entities.BranchTitle.class);
        cfg.addAnnotatedClass(md.varoinform.model.entities.Brand.class);
        cfg.addAnnotatedClass(md.varoinform.model.entities.BusinessEntityType.class);
        cfg.addAnnotatedClass(md.varoinform.model.entities.BusinessEntityTypeTitle.class);
        cfg.addAnnotatedClass(md.varoinform.model.entities.Contact.class);
        cfg.addAnnotatedClass(md.varoinform.model.entities.ContactPerson.class);
        cfg.addAnnotatedClass(md.varoinform.model.entities.Email.class);
        cfg.addAnnotatedClass(md.varoinform.model.entities.Enterprise.class);
        cfg.addAnnotatedClass(md.varoinform.model.entities.EnterpriseTitle.class);
        cfg.addAnnotatedClass(md.varoinform.model.entities.Good.class);
        cfg.addAnnotatedClass(md.varoinform.model.entities.GoodTitle.class);
        cfg.addAnnotatedClass(md.varoinform.model.entities.GProduce.class);
        cfg.addAnnotatedClass(md.varoinform.model.entities.Language.class);
        cfg.addAnnotatedClass(md.varoinform.model.entities.Person.class);
        cfg.addAnnotatedClass(md.varoinform.model.entities.PersonTitle.class);
        cfg.addAnnotatedClass(md.varoinform.model.entities.Phone.class);
        cfg.addAnnotatedClass(md.varoinform.model.entities.Position.class);
        cfg.addAnnotatedClass(md.varoinform.model.entities.PositionTitle.class);
        cfg.addAnnotatedClass(md.varoinform.model.entities.Region.class);
        cfg.addAnnotatedClass(md.varoinform.model.entities.RegionTitle.class);
        cfg.addAnnotatedClass(md.varoinform.model.entities.Sector.class);
        cfg.addAnnotatedClass(md.varoinform.model.entities.SectorTitle.class);
        cfg.addAnnotatedClass(md.varoinform.model.entities.Street.class);
        cfg.addAnnotatedClass(md.varoinform.model.entities.StreetTitle.class);
        cfg.addAnnotatedClass(md.varoinform.model.entities.Tag.class);
        cfg.addAnnotatedClass(md.varoinform.model.entities.TopAdministrativeUnit.class);
        cfg.addAnnotatedClass(md.varoinform.model.entities.TopAdministrativeUnitTitle.class);
        cfg.addAnnotatedClass(md.varoinform.model.entities.Town.class);
        cfg.addAnnotatedClass(md.varoinform.model.entities.TownTitle.class);
        cfg.addAnnotatedClass(md.varoinform.model.entities.Url.class);

        cfg.setProperty("hibernate.connection.driver_class", "org.h2.Driver");

        cfg.setProperty("hibernate.connection.url", "jdbc:h2:file:" + pathToDb);
        cfg.setProperty("hibernate.connection.username", "admin");
        cfg.setProperty("hibernate.connection.password", "password");
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

    public void setAuto(Configuration cfg, String value){
        cfg.setProperty("hibernate.hbm2ddl.auto", value);
    }

    public void showSql(Configuration cfg, boolean b){
        cfg.setProperty("hibernate.show_sql", Boolean.toString(b));

    }

}
