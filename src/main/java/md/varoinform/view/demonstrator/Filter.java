package md.varoinform.view.demonstrator;

import md.varoinform.Settings;
import md.varoinform.controller.history.History;
import md.varoinform.controller.history.HistoryEvent;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.view.dialogs.InputDialog;
import md.varoinform.view.dialogs.progress.ActivityDialog;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;

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
    //Tooltip text in header
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
                Object value = InputDialog.showInputDialog(getMessage(columnName, name), initValue, tableView.getColumnClass(column));
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

        String result = getMessage(columnName, f.name);

        if (!withValue) return result;
        return result + " " + filterValues.get(col);
    }

    private static String getMessage(String columnName, String filterName) {
        String filter = ResourceBundleHelper.getString(filterName, filterName);
        String column = ResourceBundleHelper.getString(columnName, columnName);
        String format = ResourceBundleHelper.getString("filter_input_message_format");
        return String.format(format, column, filter.toLowerCase());
    }

    private void filter(Object value, TableView tableView, int column) {
        if (value == null) return;

        RowFilter<Object, Object> rowFilter = getRowFilter(tableView.getColumnClass(column), value, column);
        if (rowFilter == null) return;
        filters.put(column, rowFilter);
        filterValues.put(column, value);
        filterTypes.put(column, this);
        setRowSorter(tableView);
    }

    private RowFilter<Object, Object> getRowFilter(Class<?> columnClass, Object value, int column){
        if (Number.class.isAssignableFrom(columnClass)) {
            Number n;
            if (value instanceof CharSequence) {
                try {
                    n = Double.parseDouble((String) value);
                } catch (NumberFormatException e){
                    e.printStackTrace();
                    return null;
                }
            } else {
                n = (Number) value;
            }
            return RowFilter.numberFilter(type, n, column);
        } else if (Date.class.isAssignableFrom(columnClass)) {
            Date d;
            if (value instanceof CharSequence){
                try {
                    d = Settings.getDefaultDateFormat().parse((String) value);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                d = (Date) value;
            }
            return RowFilter.dateFilter(type, d, column);
        } else {
            value = Pattern.quote((String) value);
            return RowFilter.regexFilter(String.format(regex, value), column);
        }
    }


    private void setRowSorter(final TableView tableView) {
        RowSorter<TableModel> sorter = ActivityDialog.start(new SwingWorker<RowSorter<TableModel>, Integer>(){
            @Override
            protected RowSorter<TableModel> doInBackground() throws Exception {
                return createSorter(tableView);
            }
        }, ResourceBundleHelper.getString("row_sorter_dialog_message"));
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
