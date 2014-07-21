package md.varoinform.util;

import md.varoinform.model.Configurator;
import md.varoinform.model.dao.EnterpriseDao;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.util.SessionManager;
import org.hibernate.Session;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class ZipTest {
    @Test
    public void testUnZip() throws Exception {
        Path path = Paths.get("src", "test", "resources", "temp.zip");
        assertTrue(Files.exists(path));

        Profiler p = new Profiler("unzip");
        Path unzip = Zip.unzip(path.toFile());
        p.end();
        assertTrue(Files.isDirectory(unzip));

        int nameCount = unzip.getNameCount();
        assertTrue(unzip.getName(nameCount - 1).toString().equals("temp"));

        File[] files = unzip.toFile().listFiles();
        assertNotNull(files);
        assertTrue(files.length == 1);

        File dbFile = files[0];
        assertEquals(dbFile.getName(), "DB.h2.db");

        Path pathToDB = Paths.get(dbFile.getParent(), "DB");
        Configurator configurator = new Configurator(pathToDB.toString());
        Session session = SessionManager.instance.getSession(configurator.configureWithoutIndex());
        assertTrue(session.isOpen());

        EnterpriseDao edao = new EnterpriseDao();
        Enterprise varo = edao.read(3083L);
        assertEquals(varo.getId(), new Long(3083L));

        SessionManager.instance.shutdownAll();
    }
}