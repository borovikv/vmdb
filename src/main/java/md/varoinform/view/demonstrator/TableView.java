package md.varoinform.view.demonstrator;

import md.varoinform.Settings;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.util.PreferencesHelper;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/14/13
 * Time: 1:05 PM
 */
public class TableView extends JTable implements Demonstrator {
    private boolean dragComplete = false;
    private TableRowSorter<TableModel> sorter;
    private Map<Integer, RowFilter<Object, Object>> filters = new HashMap<>();
    private Map<Integer, String> filtersText = new HashMap<>();

    //-----------------------------------------------------------------------------------------------------------------
    public TableView() {
        super();
        setRowHeight(24);
        setAutoCreateRowSorter(true);

        setColumnModel(new EnterpriseColumnModel());
        setModel(new EnterpriseTableModel());
        setFillsViewportHeight(true);

        getTableHeader().addMouseListener(new TableMouseAdapter());
        getColumnModel().addColumnModelListener(new ColumnModelListener());
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger() && e.getComponent() instanceof JTable ) {
                    int i = TableView.this.columnAtPoint(e.getPoint());

                    JPopupMenu popup = createPopup(i);
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        setSelectionBackground(Settings.getDefaultColor("highlight"));

        showResults(new ArrayList<Enterprise>());
    }

    private JPopupMenu createPopup(final int column) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem filter = new JMenuItem("filter");
        filter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String result = JOptionPane.showInputDialog("filter by", filtersText.get(column));
                newFilter(result, ".*" +result  + ".*", column);
            }
        });
        popupMenu.add(filter);
        JMenuItem notContainFilter = new JMenuItem("not contain");
        notContainFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String result = JOptionPane.showInputDialog("filter by", filtersText.get(column));

                newFilter(result, "^((?!" + result + ").)*$", column);
            }
        });
        popupMenu.add(notContainFilter);
        return popupMenu;
    }

    private void newFilter(String text, String pattern, int column) {

        RowFilter<TableModel, Object> rf;
        //If current expression doesn't parse, don't update.
        try {
            filters.put(column, RowFilter.regexFilter(pattern , column));
            filtersText.put(column, text);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        rf = RowFilter.andFilter(filters.values());
        sorter.setRowFilter(rf);
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
        doLayout();
    }

    @Override
    public void setModel(TableModel dataModel) {
        super.setModel(dataModel);
        sorter = new TableRowSorter<>(dataModel);
        setRowSorter(sorter);
    }

    @Override
    public List<Enterprise> getSelected() {
        List<Enterprise> enterprises = new ArrayList<>();

        for (int index : getSelectedRows()) {

            if (isIndexOutOfBound(index)) continue;

            enterprises.add(getEnterpriseAt(index));
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
