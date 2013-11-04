package md.varoinform.controller;

import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.search.SearchEngine;
import md.varoinform.model.util.HibernateSessionFactory;
import org.hibernate.Session;

import javax.swing.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/4/13
 * Time: 11:41 AM
 */
public class SearchProxy implements Proxy<String> {
    private final HistoryProxy historyProxy;
    private final Demonstrator demonstrator;

    public SearchProxy(Demonstrator demonstrator, HistoryProxy historyProxy) {
        this.demonstrator = demonstrator;
        this.historyProxy = historyProxy;
    }

    @Override
    public void perform(String value) {

        SearchEngine se = new SearchEngine();
        List<Enterprise> enterprises = se.search(value);

        demonstrator.showResults(enterprises);
        historyProxy.append(enterprises);
    }
}
