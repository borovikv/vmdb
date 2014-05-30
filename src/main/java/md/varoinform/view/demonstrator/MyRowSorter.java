package md.varoinform.view.demonstrator;

import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 5/30/14
 * Time: 5:02 PM
 */
public class MyRowSorter<M extends TableModel> extends TableRowSorter<M> {
    private Set<Integer> columns = new HashSet<>();

    public MyRowSorter(M model) {
        super(model);
    }

    public Set<Integer> getColumns() {
        return columns;
    }

    public void setColumns(Set<Integer> columns) {
        this.columns = columns;
    }
}
