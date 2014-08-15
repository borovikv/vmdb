package md.varoinform;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import md.varoinform.controller.cache.Cache;
import md.varoinform.util.Profiler;
import md.varoinform.view.demonstrator.EnterpriseView;

import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        //assertTrue( true );
        Profiler p = new Profiler("all");
        long max = 0;
        double nano = 1000_000_000.0;
        String result = "";
        List<Long> allEnterpriseIds = Cache.instance.getAllEnterpriseIds();
        EnterpriseView.getView(allEnterpriseIds.get(0));
        for (Long aLong : allEnterpriseIds) {
            long l = System.nanoTime();
            String s = EnterpriseView.getView(aLong);
            long elapsed = System.nanoTime() - l;
            double x = elapsed / nano;
            if (x >0.1){
                System.out.println(x);
                System.out.println(s);
            }

            if (elapsed > max) {
                max = elapsed;
                result = s;
            }
        }
        p.end();
        System.out.println(max / nano);
        System.out.println(result);
    }


}
