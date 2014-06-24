package md.varoinform.controller;

import md.varoinform.util.Profiler;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 6/24/14
 * Time: 4:25 PM
 */
public class Holder implements Closeable {
    private static boolean wait = false;
    private final Profiler p;

    public Holder() {
        wait = true;
        p = new Profiler("hold");
    }

    @Override
    public void close() throws IOException {
        wait = false;
        p.end();
    }

    public static boolean isWait() {
        return wait;
    }
}
