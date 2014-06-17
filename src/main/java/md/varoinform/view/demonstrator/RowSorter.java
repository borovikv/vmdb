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
public class RowSorter<M extends TableModel> extends TableRowSorter<M> {
    private Set<Integer> filteredColumns = new HashSet<>();

    public RowSorter(M model) {
        super(model);
    }

    public Set<Integer> getFilteredColumns() {
        return filteredColumns;
    }

    public void setFilteredColumns(Set<Integer> filteredColumns) {
        this.filteredColumns = filteredColumns;
    }

}
