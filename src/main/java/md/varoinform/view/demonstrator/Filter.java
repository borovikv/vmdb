package md.varoinform.view.demonstrator;

import md.varoinform.controller.history.History;
import md.varoinform.controller.history.HistoryEvent;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.view.dialogs.InputDialog;
import md.varoinform.view.dialogs.progress.ActivityDialog;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
* Created with IntelliJ IDEA.
* User: Vladimir Borovic
* Date: 5/13/14
* Time: 3:43 PM
*/
enum Filter {

    CONTAIN("(?u)(?i).*%s.*", "contain"),
    NOT_CONTAIN("(?u)(?i)^((?!%s).)*$", "not_contain"),
    AFTER(RowFilter.ComparisonType.AFTER, "after"),
    BEFORE(RowFilter.ComparisonType.BEFORE, "before"),
    EQUAL(RowFilter.ComparisonType.EQUAL, "equal"),
    NOT_EQUAL(RowFilter.ComparisonType.NOT_EQUAL, "not_equal"),
    EMPTY("^\\s*$", "empty"),
    NOT_EMPTY("^.+$", "not_empty"),
    REMOVE("", null);

    private static Map<Integer, RowFilter<Object, Object>> filters = new HashMap<>();
    private static Map<Integer, Object> filterValues = new HashMap<>();
    private static Map<Integer, Filter> filterTypes = new HashMap<>();
    private final String regex;
    private final RowFilter.ComparisonType type;
    private final String name;

    Filter(String regex, String name) {
        this.regex = regex;
        this.name = name;
        type = null;
    }

    Filter(RowFilter.ComparisonType type, String name) {
        this.type = type;
        this.name = name;
        regex = "";
    }
    public static List<ActionListener> getListeners(Class<?> columnClass, TableView tableView, int column, Object value){
        if (CharSequence.class.isAssignableFrom(columnClass)){
            return Arrays.asList(
                    CONTAIN.getFilterListener(tableView, column, value),
                    NOT_CONTAIN.getFilterListener(tableView, column, value),
                    EMPTY.getEmptyFilterListener(tableView, column),
                    NOT_EMPTY.getEmptyFilterListener(tableView, column)
            );
        } else if (Number.class.isAssignableFrom(columnClass) || Date.class.isAssignableFrom(columnClass)) {
            return Arrays.asList(
                    AFTER.getFilterListener(tableView, column, value),
                    BEFORE.getFilterListener(tableView, column, value),
                    EQUAL.getFilterListener(tableView, column, value),
                    NOT_EQUAL.getFilterListener(tableView, column, value),
                    EMPTY.getEmptyFilterListener(tableView, column),
                    NOT_EMPTY.getEmptyFilterListener(tableView, column)
            );
        }
        return new ArrayList<>();
    }

    public ActionListener getFilterListener(final TableView tableView, final int column, final Object colVal){
        return new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                String columnName = tableView.getColumnName(column);
                Object initValue = colVal;
                if (filterValues.containsKey(column)){
                    initValue = filterValues.get(column);
                }
                Object value = InputDialog.showInputDialog(getMessage(column, columnName, false), initValue);
                filter(value, tableView, column);
            }

            @Override
            public String toString() {
                return name;
            }
        };
    }

    public static String getMessage(int col, String columnName, boolean withValue) {
        Filter f = filterTypes.get(col);
        if (f == null) return "";

        String filterName = f.name;
        String filter = ResourceBundleHelper.getString(filterName, filterName);
        String column = ResourceBundleHelper.getString(columnName, columnName);
        String format = ResourceBundleHelper.getString("filter-by");
        String result = String.format(format, column, filter.toLowerCase());
        if (!withValue) return result;
        return result + " " + filterValues.get(col);
    }

    private void filter(Object value, TableView tableView, int column) {
        if (value == null) return;

        RowFilter<Object, Object> rowFilter = getRowFilter(value, column);
        if (rowFilter == null) return;

        filters.put(column, rowFilter);
        filterValues.put(column, value);
        filterTypes.put(column, this);
        setRowSorter(tableView);
    }

    private void setRowSorter(final TableView tableView) {
        RowSorter<TableModel> sorter = ActivityDialog.start(new SwingWorker<RowSorter<TableModel>, Integer>(){
            @Override
            protected RowSorter<TableModel> doInBackground() throws Exception {
                return createSorter(tableView);
            }
        }, ResourceBundleHelper.getString("sorter-dialog-message"));
        if (sorter == null) return;
        tableView.setRowSorter(sorter);
        History.instance.add(new HistoryEvent(this, sorter));
    }

    private RowSorter<TableModel> createSorter(TableView tableView) {
        RowFilter<TableModel, Object> andFilter = RowFilter.andFilter(filters.values());
        RowSorter<TableModel> sorter = new RowSorter<>(tableView.getModel());
        sorter.setRowFilter(andFilter);
        sorter.setFilteredColumns(filters.keySet());
        return sorter;
    }


    public ActionListener getEmptyFilterListener(final TableView tableView, final int column){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RowFilter<Object, Object> filter = RowFilter.regexFilter(regex, column);
                filters.put(column, filter);
                filterTypes.put(column, Filter.this);
                setRowSorter(tableView);

            }

            @Override
            public String toString() {
                return name;
            }
        };
    }

    private RowFilter<Object, Object> getRowFilter(Object value, int column){
        if (value instanceof Number) {
            return RowFilter.numberFilter(type, (Number) value, column);
        }
        if (value instanceof Date) {
            return RowFilter.dateFilter(type, (Date)value, column);
        }
        if (value instanceof CharSequence) {
            String val = ((String)value).replace("[", "\\[");
            value = val.replace("]", "\\]");
            return RowFilter.regexFilter(String.format(regex, value), column);
        }
        return null;
    }

    public static void clear() {
        filters.clear();
        filterValues.clear();
        filterTypes.clear();
    }

    public static void remove(TableView demonstrator, int column) {
        filters.remove(column);
        filterValues.remove(column);
        filterTypes.remove(column);
        REMOVE.setRowSorter(demonstrator);
    }
}