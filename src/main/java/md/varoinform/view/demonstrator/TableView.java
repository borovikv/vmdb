package md.varoinform.view.demonstrator;

import md.varoinform.model.entities.Enterprise;
import md.varoinform.util.ResourceBundleHelper;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/14/13
 * Time: 1:05 PM
 */
public class TableView extends JTable implements Demonstrator {

    private final TableColumnModelListener tableColumnModelListener = new TableColumnModelListener() {
        @Override
        public void columnAdded(TableColumnModelEvent e) {
        }

        @Override
        public void columnRemoved(TableColumnModelEvent e) {
        }

        @Override
        public void columnMoved(TableColumnModelEvent e) {
            dragComplete = true;
        }

        @Override
        public void columnMarginChanged(ChangeEvent e) {
        }

        @Override
        public void columnSelectionChanged(ListSelectionEvent e) {
        }
    };

    private final MouseAdapter mouseAdapter = new MouseAdapter() {
        @Override
        public void mouseReleased(MouseEvent e) {
            if (dragComplete) {
                columnOrderChanged();
            }
            dragComplete = false;
        }
    };

    private boolean dragComplete;

    //-----------------------------------------------------------------------------------------------------------------
    public TableView() {
        super();
        setFillsViewportHeight(true);
        setRowHeight(24);
        setAutoCreateRowSorter(true);
        setModel(new EnterpriseTableModel());
        setColumnModel(new EnterpriseColumnModel());
        getTableHeader().addMouseListener(mouseAdapter);
        getColumnModel().addColumnModelListener(tableColumnModelListener);
        ResourceBundle bundle = ResourceBundle.getBundle("VaroDB");
        Color highlightColor = (Color)bundle.getObject("highlightColor");
        setSelectionBackground(highlightColor);
        showResults(new ArrayList<Enterprise>());
    }

    private void columnOrderChanged() {
        String columns = getColumns();

        Preferences preferences = Preferences.userNodeForPackage(md.varoinform.App.class);
        preferences.put("columns", columns);

        fireViewStructureChanged();
    }

    private String getColumns() {
        DefaultTableColumnModel tableColumnModel = (DefaultTableColumnModel) getColumnModel();
        int columnCount = tableColumnModel.getColumnCount();
        StringBuilder buf = new StringBuilder();
        String delimiter = "";
        for (int i = 0; i < columnCount; i++) {
            TableColumn column = tableColumnModel.getColumn(i);
            Object value = column.getIdentifier();
            buf.append(delimiter);
            buf.append(value);
            delimiter = ";";
        }
        return buf.toString();
    }

    public void fireViewStructureChanged() {
        ((AbstractTableModel)getModel()).fireTableStructureChanged();
    }

    public void addListSelectionListener(ListSelectionListener listener){
        ListSelectionModel rowSM = getSelectionModel();
        rowSM.addListSelectionListener(listener);
    }


    @Override
    public void showResults(List<Enterprise> enterprises) {
        if (enterprises == null) {
            enterprises = new ArrayList<>();
        }
        EnterpriseTableModel model = new EnterpriseTableModel(enterprises);
        setModel(model);
        doLayout();
    }


    @Override
    public List<Enterprise> getSelected() {
        List<Enterprise> enterprises = new ArrayList<>();
        for (int index : getSelectedRows()) {
            if ( isIndexOutOfBound(index) ) continue;
            enterprises.add( getEnterpriseAt( index ) );
        }
        return enterprises;
    }

    private boolean isIndexOutOfBound(int selectedRow) {
        EnterpriseTableModel model = getEnterpriseTableModel();
        return 0 > selectedRow || selectedRow > model.getRowCount();
    }

    private Enterprise getEnterpriseAt(int rowIndex) {
        return getEnterpriseTableModel().getEnterpriseAt(rowIndex);
    }

    private EnterpriseTableModel getEnterpriseTableModel() {
        return ((EnterpriseTableModel)getModel());
    }


    @Override
    public List<Enterprise> getALL() {
        return getEnterpriseTableModel().getEnterprises();
    }


    @Override
    public void clear() {
        ((DefaultTableModel)getModel()).setRowCount(0);
    }


    @Override
    public Enterprise getSelectedEnterprise() {
        int rowIndex = getRowIndex();
        if ( isIndexOutOfBound(rowIndex) ) return null;
        return getEnterpriseAt(rowIndex);
    }

    private int getRowIndex() {
        int selectedRow = getSelectedRow();
        if ( !isIndexOutOfBound( selectedRow ) ) {
            return convertRowIndexToModel( selectedRow );
        }
        return -1;
    }


    @Override
    public void updateUI() {
        super.updateUI();
        updateColumnNames();
    }

    private void updateColumnNames() {
        TableColumnModel model = getColumnModel();

        int columnCount = model.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            TableColumn column = model.getColumn(i);
            String identifier = (String)column.getIdentifier();
            String columnName = ResourceBundleHelper.getString(identifier, identifier);
            column.setHeaderValue(columnName);
        }

        getTableHeader().repaint();
    }
}
