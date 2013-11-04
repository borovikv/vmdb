package md.varoinform.util;

import md.varoinform.controller.Proxy;

import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/4/13
 * Time: 12:10 PM
 */
public abstract class AbstractProxyListener<T> implements ActionListener {
    protected Proxy<T> proxy;

    public AbstractProxyListener(Proxy<T> proxy) {
        this.proxy = proxy;
    }

}
