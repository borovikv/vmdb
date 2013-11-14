package md.varoinform.view;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/14/13
 * Time: 4:47 PM
 */
public class EnterpriseTableModel extends DefaultTableModel {

    public EnterpriseTableModel(Object[] columnNames, int rowCount) {
        super(columnNames, rowCount);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
