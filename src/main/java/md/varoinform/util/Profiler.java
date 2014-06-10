package md.varoinform.util;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 6/10/14
 * Time: 10:58 AM
 */
public class Profiler {
    private final long start;

    public Profiler() {
        start = System.nanoTime();
    }

    public double end(){
        long end = System.nanoTime();
        double result = (end - start)/1000000000.0;
        System.out.println(result);
        return result;
    }
}
