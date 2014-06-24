package md.varoinform.view.demonstrator;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/4/13
 * Time: 11:44 AM
 */
public interface Demonstrator {

    public void showResults(List<Long> enterprises);

    public List<Long> getSelected();

    public List<Long> getALL();
}
