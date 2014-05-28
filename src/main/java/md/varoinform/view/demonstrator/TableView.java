package md.varoinform.view.demonstrator;

import md.varoinform.Settings;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.util.PreferencesHelper;
import md.varoinform.view.status.OutputLabel;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/14/13
 * Time: 1:05 PM
 */
public class TableView extends JTable implements Demonstrator {
    private boolean dragComplete = false;



    //-----------------------------------------------------------------------------------------------------------------
    public TableView() {
        super();
        setRowHeight(24);
        setAutoCreateRowSorter(true);

        setDefaultRenderer(Date.class, new DataCellRenderer());
        setColumnModel(new EnterpriseColumnModel());
        setModel(new EnterpriseTableModel());
        setFillsViewportHeight(true);

        getTableHeader().addMouseListener(new TableMouseAdapter());
        getColumnModel().addColumnModelListener(new ColumnModelListener());

        setSelectionBackground(Settings.getDefaultColor("highlight"));
        setFont(Settings.getDefaultFont("SANS_SERIF", 12));
        setColumnSelectionAllowed(false);
        setRowSelectionAllowed(true);

        setDragEnabled(true);
        setTransferHandler(new EnterpriseTransferableHandler());
    }



    public void fireViewStructureChanged() {

        ((AbstractTableModel)getModel()).fireTableStructureChanged();
    }

    public void addListSelectionListener(ListSelectionListener listener){

        ListSelectionModel selectionModel = getSelectionModel();
        selectionModel.addListSelectionListener(listener);
    }


    @Override
    public void showResults(List<Enterprise> enterprises) {

        if (enterprises == null) {
            enterprises = new ArrayList<>();
        }

        EnterpriseTableModel dataModel = new EnterpriseTableModel(enterprises);
        setModel(dataModel);

        //doLayout();
        OutputLabel.instance.setResultCount(getRowCount());
    }

    @Override
    public void setRowSorter(RowSorter<? extends TableModel> sorter) {
        super.setRowSorter(sorter);
        OutputLabel.instance.setResultCount(getRowCount());
    }

    @Override
    public List<Enterprise> getSelected() {
        List<Enterprise> enterprises = new ArrayList<>();

        for (int index : getSelectedRows()) {
            int realIndex = convertRowIndexToModel(index);

            if (isIndexOutOfBound(realIndex)) continue;

            enterprises.add(getEnterpriseAt(realIndex));
        }

        return enterprises;
    }

    private boolean isIndexOutOfBound(int selectedRow) {

        return 0 > selectedRow || selectedRow > getEnterpriseTableModel().getRowCount();
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

        setModel(new EnterpriseTableModel());
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


    public void updateDisplay() {
        updateUI();
        updateColumnNames();
    }

    private void updateColumnNames() {
        EnterpriseColumnModel columnModel = (EnterpriseColumnModel) getColumnModel();
        columnModel.updateHeader();

        getTableHeader().repaint();
    }

    @Override
    public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
        // if row already selected remove selection from current row
        boolean rowSelected = isRowSelected(rowIndex);
        super.changeSelection(rowIndex, columnIndex, toggle || rowSelected, extend);
    }

    private class TableMouseAdapter extends MouseAdapter {
        @Override
        public void mouseReleased(MouseEvent e) {
            if (dragComplete) {
                columnOrderChanged();
            }
            dragComplete = false;
        }

        private void columnOrderChanged() {
            PreferencesHelper helper = new PreferencesHelper();
            helper.putUserFields(getColumns());

            fireViewStructureChanged();
        }

        private List<String> getColumns() {
            Enumeration<TableColumn> columns = getColumnModel().getColumns();

            List<String> columnNames = new ArrayList<>();
            while (columns.hasMoreElements()){
                String columnIdentifier = (String) columns.nextElement().getIdentifier();
                columnNames.add(columnIdentifier);
            }

            return columnNames;
        }
    }


    private class ColumnModelListener implements TableColumnModelListener {
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
    }
}
