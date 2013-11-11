package md.varoinform.controller;

import md.varoinform.model.entities.Enterprise;

import java.util.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/7/13
 * Time: 12:14 PM
 */
public class SortingProxy implements Proxy< Comparator< Enterprise > > {
    private Demonstrator demonstrator;

    public SortingProxy(Demonstrator demonstrator) {
        this.demonstrator = demonstrator;
    }

    @Override
    public void perform(Comparator<Enterprise> value) {

    }
}
