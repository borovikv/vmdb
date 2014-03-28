package md.varoinform.update;

import md.varoinform.sequrity.UnregisteredDBExertion;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 3/28/14
 * Time: 2:39 PM
 */
public class UpdaterTest {
    @Test
    @Ignore
    public void testCheckUpdate() throws Exception, UnregisteredDBExertion {
        boolean result = new Updater().checkUpdate();
        System.out.println(result);
//        Assert.assertTrue(result);

    }

    @Test
    public void testParseResponse() throws NoSuchMethodException, UnregisteredDBExertion, InvocationTargetException, IllegalAccessException {
        Method method = Updater.class.getDeclaredMethod("parseContent", String.class);
        method.setAccessible(true);
        Assert.assertTrue( (boolean) method.invoke(new Updater(), "Value=Yes"));
        Assert.assertTrue( (boolean) method.invoke(new Updater(), "value=yes"));
        Assert.assertTrue( (boolean) method.invoke(new Updater(), "VALUE=YES"));
        Assert.assertTrue( (boolean) method.invoke(new Updater(), "VALUE=true"));
    }
}
