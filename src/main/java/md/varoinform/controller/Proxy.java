package md.varoinform.controller;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/4/13
 * Time: 11:32 AM
 */
public interface Proxy<T> {
    public void perform(T value);
}
