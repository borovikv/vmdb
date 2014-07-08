package md.varoinform.update;

import md.varoinform.sequrity.exception.UnregisteredDBExertion;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
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
    public void testParseResponse() throws NoSuchMethodException, UnregisteredDBExertion, InvocationTargetException, IllegalAccessException {
        Method method = getMethodParseContent();
        Assert.assertTrue( (boolean) method.invoke(new Updater(), "Value=Yes"));
        Assert.assertTrue( (boolean) method.invoke(new Updater(), "value=yes"));
        Assert.assertTrue((boolean) method.invoke(new Updater(), "VALUE=YES"));
    }

    public Method getMethodParseContent() throws NoSuchMethodException {
        Method method = Updater.class.getDeclaredMethod("parseContent", String.class);
        method.setAccessible(true);
        return method;
    }

    @Test(expected = IOException.class)
    public void testFailParseResponse() throws Throwable {
        try {
            getMethodParseContent().invoke(new Updater(), "VALUE=true");
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
