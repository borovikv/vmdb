package md.varoinform.update;

import md.varoinform.sequrity.exception.UnregisteredDBExertion;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 3/28/14
 * Time: 2:39 PM
 */
public class UpdaterTest {
    private static final String uid = "TEST";

    @Test
    public void testParseResponse() throws NoSuchMethodException, UnregisteredDBExertion, InvocationTargetException, IllegalAccessException {
        Method method = getMethodParseContent();
        assertTrue((boolean) method.invoke(new Updater(uid), "Value=Yes"));
        assertTrue((boolean) method.invoke(new Updater(uid), "value=yes"));
        assertTrue((boolean) method.invoke(new Updater(uid), "VALUE=YES"));
    }

    public Method getMethodParseContent() throws NoSuchMethodException {
        Method method = Updater.class.getDeclaredMethod("parseContent", String.class);
        method.setAccessible(true);
        return method;
    }

    @Test(expected = IOException.class)
    public void testFailParseResponse() throws Throwable {
        try {
            getMethodParseContent().invoke(new Updater(uid), "VALUE=true");
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
