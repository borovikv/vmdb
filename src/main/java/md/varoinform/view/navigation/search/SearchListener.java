package md.varoinform.view.navigation.search;

import md.varoinform.model.entities.Enterprise;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 5/15/14
 * Time: 11:57 AM
 */
public interface SearchListener {
    public void perform(List<Enterprise> enterprises);
}
