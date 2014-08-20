package md.varoinform.controller;

import md.varoinform.util.Profiler;

import java.io.Closeable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 6/24/14
 * Time: 4:25 PM
 */
public class Holder implements Closeable {
    private static AtomicBoolean wait = new AtomicBoolean(false);
    private static AtomicInteger counter = new AtomicInteger(0);
    private final Profiler p;

    public Holder() {
        wait.set(true);
        counter.addAndGet(1);
        p = new Profiler("hold");
    }

    @Override
    public synchronized void close() {
        int currentCounter = counter.decrementAndGet();
        if (currentCounter == 0) wait.set(false);
        p.end();
    }

    public static boolean await() {
        return wait.get();
    }
}
