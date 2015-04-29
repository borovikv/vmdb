package md.varoinform.util;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 6/10/14
 * Time: 10:58 AM
 */
@SuppressWarnings("UnusedDeclaration")
public class Profiler {
    private final Long start;
    private final String text;

    public Profiler(String text) {
        this.text = text;
        start = System.nanoTime();
    }

    public double end(){
        Long end = System.nanoTime();
        double result = (end - start)/1000_000_000.0;
        System.out.println(text + " = " + result);
        return result;
    }
}
