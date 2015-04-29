package md.varoinform.model.search;

import md.varoinform.model.dao.EnterpriseDao;
import md.varoinform.model.utils.Configurator;
import md.varoinform.sequrity.PasswordManager;
import md.varoinform.util.Profiler;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 7/22/14
 * Time: 4:20 PM
 */
public class TestEnterpriseDao {
    @Test
    public void testCount() {
        Profiler p = new Profiler("count by date test");
        Path path = Paths.get("src", "test", "resources", "updateTest", "DB");
        Configurator configurator = new Configurator(path.toString(), PasswordManager.getPassword());
        Configuration cfg = configurator.defaultConfiguration();

        //Tue Jul 22 00:00:00 EEST 2014
        Date checkDate = new Date(1405976400000L);
        Date date = EnterpriseDao.getMaxCheckDate(cfg);
        assertNotNull(date);
        assertEquals(checkDate,  date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, -1);
        Date date1 = calendar.getTime();
        Integer amount = EnterpriseDao.countWhereLastChangeGTE(cfg, date1);
        assertEquals(amount.intValue(), 1);
        p.end();
    }
}
