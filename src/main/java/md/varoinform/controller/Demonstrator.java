package md.varoinform.controller;

import md.varoinform.model.entities.Enterprise;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/4/13
 * Time: 11:44 AM
 */
public interface Demonstrator {
    public void showResults(List<Enterprise> enterprises);
    public List<Enterprise> getSelected();
}
