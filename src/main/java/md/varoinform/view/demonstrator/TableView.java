package md.varoinform.view.demonstrator;

import md.varoinform.Settings;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.util.ImageHelper;
import md.varoinform.util.PreferencesHelper;
import md.varoinform.view.status.OutputLabel;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
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
        setAutoResizeMode(AUTO_RESIZE_OFF);
        setDefaultRenderer(Date.class, new DataCellRenderer());
        getTableHeader().setDefaultRenderer(new HeaderRenderer());
        setColumnModel(new EnterpriseColumnModel());
        setFillsViewportHeight(true);

        getTableHeader().addMouseListener(new TableMouseAdapter());
        getColumnModel().addColumnModelListener(new ColumnModelListener());

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
    public void doLayout()
    {
        TableColumn resizingColumn = null;

        if (tableHeader != null)
            resizingColumn = tableHeader.getResizingColumn();

        //  Viewport size changed. May need to increase columns widths

        if (resizingColumn == null)
        {
            setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            super.doLayout();
        }

        //  Specific column resized. Reset preferred widths

        else
        {
            TableColumnModel tcm = getColumnModel();

            for (int i = 0; i < tcm.getColumnCount(); i++)
            {
                TableColumn tc = tcm.getColumn(i);
                tc.setPreferredWidth( tc.getWidth() );
            }

            // Columns don't fill the viewport, invoke default layout

            if (tcm.getTotalColumnWidth() < getParent().getWidth())
                setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            super.doLayout();
        }

        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
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

        FilterListener.clear();
        //doLayout();
        OutputLabel.instance.setResultCount(getRowCount());
    }

    @Override
    public void setRowSorter(RowSorter<? extends TableModel> sorter) {
        super.setRowSorter(sorter);
        int columnCount = getModel().getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            removeFilterIcon(i);
        }
        if (sorter instanceof MyRowSorter) {
            @SuppressWarnings("unchecked") Set<Integer> columns = ((MyRowSorter) sorter).getColumns();
            for (Integer column : columns) {
                setFilterIcon(column);
            }
        }
        OutputLabel.instance.setResultCount(getRowCount());
    }

    public void setFilterIcon(int column){
        ((HeaderRenderer)getTableHeader().getDefaultRenderer()).addIndex(column);
    }

    public void removeFilterIcon(int column){
        ((HeaderRenderer)getTableHeader().getDefaultRenderer()).removeIndex(column);
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

    private class HeaderRenderer implements TableCellRenderer {
        private Set<Integer> indexes = new HashSet<>();

        public void addIndex(int column){
            indexes.add(column);
            getTableHeader().updateUI();
        }

        public void removeIndex(int column){
            indexes.remove(column);
            getTableHeader().updateUI();
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = new JLabel(String.valueOf(value));
            label.setFont(new Font(Settings.Fonts.SANS_SERIF.getName(), Font.BOLD, 14));
            label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            label.setOpaque(true);
            if (indexes.contains(column)) {
                FontMetrics fontMetrics = getFontMetrics(getFont());
                int size = fontMetrics.getHeight();
                ImageIcon icon = ImageHelper.getScaledImageIcon("/external-resources/icons/filter.png", size, size);
                label.setIcon(icon);
            }
            return label;
        }
    }
}
