package md.varoinform.view.demonstrator;

import md.varoinform.Settings;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.util.PreferencesHelper;
import md.varoinform.view.status.StatusBar;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
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
        setAutoResizeMode(AUTO_RESIZE_OFF);
        setDefaultRenderer(Date.class, new DataCellRenderer());
        getTableHeader().setDefaultRenderer(new HeaderRenderer());
        setColumnModel(new EnterpriseColumnModel());
        setFillsViewportHeight(true);

        setSelectionBackground(Settings.getDefaultColor("highlight"));
        setFont(Settings.getDefaultFont(Settings.Fonts.SANS_SERIF, 12));
        setColumnSelectionAllowed(false);
        setRowSelectionAllowed(true);

        setDragEnabled(true);
        setTransferHandler(new EnterpriseTransferableHandler());

        setModel(new EnterpriseTableModel());
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return getPreferredSize().width < getParent().getWidth();
    }

    @Override
    public void doLayout() {
        TableColumn resizingColumn = null;

        if (tableHeader != null)
            resizingColumn = tableHeader.getResizingColumn();

        //  Viewport size changed. May need to increase columns widths

        if (resizingColumn == null) {
            setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            super.doLayout();
        }

        //  Specific column resized. Reset preferred widths

        else {
            TableColumnModel tcm = getColumnModel();

            for (int i = 0; i < tcm.getColumnCount(); i++) {
                TableColumn tc = tcm.getColumn(i);
                tc.setPreferredWidth(tc.getWidth());
            }

            // Columns don't fill the viewport, invoke default layout

            if (tcm.getTotalColumnWidth() < getParent().getWidth())
                setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            super.doLayout();
        }

        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    public void fireViewStructureChanged() {
        ((AbstractTableModel) getModel()).fireTableStructureChanged();
    }

    public void addListSelectionListener(ListSelectionListener listener) {

        ListSelectionModel selectionModel = getSelectionModel();
        selectionModel.addListSelectionListener(listener);
    }


    @Override
    public void showResults(List<Enterprise> enterprises) {
        if (enterprises == null) {
            enterprises = new ArrayList<>();
        }
        setRowSorter(null);
        EnterpriseTableModel dataModel = new EnterpriseTableModel(enterprises);
        setModel(dataModel);
        RowSorter<EnterpriseTableModel> sorter = new RowSorter<>(dataModel);
        setRowSorter(sorter);

        FilterListener.clear();
        StatusBar.instance.setTotal(getRowCount());
        StatusBar.instance.setRow(0);
    }

    @Override
    public void setRowSorter(javax.swing.RowSorter<? extends TableModel> sorter) {
        TableCellRenderer renderer = getTableHeader().getDefaultRenderer();
        if (!(renderer instanceof HeaderRenderer)){
            renderer = new HeaderRenderer();
            getTableHeader().setDefaultRenderer(renderer);
        }
        if (sorter instanceof RowSorter) {
            //noinspection unchecked
            ((HeaderRenderer)renderer).setFilteredColumns(((RowSorter) sorter).getFilteredColumns());
        } else {
            ((HeaderRenderer)renderer).setFilteredColumns(new HashSet<Integer>());
        }
        super.setRowSorter(sorter);
        StatusBar.instance.setTotal(getRowCount());
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
        return ((EnterpriseTableModel) getModel());
    }


    @Override
    public List<Enterprise> getALL() {
        List<Enterprise> enterprises = new ArrayList<>();
        for (int i = 0; i < getRowCount(); i++) {
            int realIndex = convertRowIndexToModel(i);

            if (isIndexOutOfBound(realIndex)) continue;

            enterprises.add(getEnterpriseAt(realIndex));

        }
        return enterprises;
    }


    @Override
    public void clear() {
        setModel(new EnterpriseTableModel());
    }


    @Override
    public Enterprise getSelectedEnterprise() {
        int rowIndex = getRowIndex();
        if (isIndexOutOfBound(rowIndex)) return null;
        return getEnterpriseAt(rowIndex);
    }

    private int getRowIndex() {

        int selectedRow = getSelectedRow();

        if (!isIndexOutOfBound(selectedRow)) {
            return convertRowIndexToModel(selectedRow);
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

    public void columnOrderChanged() {
        if (dragComplete) {
            PreferencesHelper helper = new PreferencesHelper();
            helper.putUserFields(getColumns());

            fireViewStructureChanged();
        }
        dragComplete = false;
    }

    private List<String> getColumns() {
        Enumeration<TableColumn> columns = getColumnModel().getColumns();

        List<String> columnNames = new ArrayList<>();
        while (columns.hasMoreElements()) {
            String columnIdentifier = (String) columns.nextElement().getIdentifier();
            columnNames.add(columnIdentifier);
        }

        return columnNames;
    }


    public void columnMoved() {
        dragComplete = true;
    }

    public void sort(int column, RowSorterWorker.SortingType type) {
        ((EnterpriseTableModel) getModel()).sort(column, type);
        Map<Integer, RowSorterWorker.SortingType> sortedColumns = new HashMap<>();
        sortedColumns.put(column, type);
        ((HeaderRenderer) getTableHeader().getDefaultRenderer()).setSortedColumns(sortedColumns);
    }

}
