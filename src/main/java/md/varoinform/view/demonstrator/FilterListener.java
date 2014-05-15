package md.varoinform.view.demonstrator;

import md.varoinform.controller.history.History;
import md.varoinform.controller.history.HistoryEvent;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
* Created with IntelliJ IDEA.
* User: Vladimir Borovic
* Date: 5/13/14
* Time: 3:43 PM
*/
enum FilterListener {

    CONTAIN("(?u)(?i).*%s.*", "contain"),
    NOT_CONTAIN("(?u)(?i)^((?!%s).)*$", "not_contain"),
    AFTER(RowFilter.ComparisonType.AFTER, "after"),
    BEFORE(RowFilter.ComparisonType.BEFORE, "before"),
    EQUAL(RowFilter.ComparisonType.EQUAL, "equal"),
    NOT_EQUAL(RowFilter.ComparisonType.NOT_EQUAL, "not_equal");

    private static Map<Integer, RowFilter<Object, Object>> filters = new HashMap<>();
    private static Map<Integer, String> filtersText = new HashMap<>();
    private final String regex;
    private final RowFilter.ComparisonType type;
    private final String name;

    FilterListener(String regex, String name) {
        this.regex = regex;
        this.name = name;
        type = null;
    }

    FilterListener(RowFilter.ComparisonType type, String name) {
        this.type = type;
        this.name = name;
        regex = "";
    }
    public static List<ActionListener> getListeners(Class<?> columnClass, TableView tableView, int column, Object value){
        if (CharSequence.class.isAssignableFrom(columnClass)){
            return Arrays.asList(
                    CONTAIN.getFilterListener(columnClass, tableView, column, value),
                    NOT_CONTAIN.getFilterListener(columnClass, tableView, column, value));
        } else if (Number.class.isAssignableFrom(columnClass) || Date.class.isAssignableFrom(columnClass)) {
            return Arrays.asList(
                    AFTER.getFilterListener(columnClass, tableView, column, value),
                    BEFORE.getFilterListener(columnClass, tableView, column, value),
                    EQUAL.getFilterListener(columnClass, tableView, column, value),
                    NOT_EQUAL.getFilterListener(columnClass, tableView, column, value)
            );
        }
        return new ArrayList<>();
    }

    public ActionListener getFilterListener(final Class<?> columnClass, final TableView tableView, final int column, final Object value){
        return new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {

                String filter = filtersText.get(column);

                String result = JOptionPane.showInputDialog("filter by", filter == null ? value : filter);
                filter(result, columnClass, tableView, column);
            }

            @Override
            public String toString() {
                return name;
            }
        };
    }


    private void filter(String text, Class<?> columnClass, TableView tableView, int column) {
        if (text == null || text.isEmpty() && filters.containsKey(column)) {
            filters.remove(column);
        } else {
            RowFilter<Object, Object> rowFilter = getRowFilter(text, columnClass, column);
            if (rowFilter == null) return;

            filters.put(column, rowFilter);
            filtersText.put(column, text);
        }
        RowFilter<TableModel, Object> andFilter = RowFilter.andFilter(filters.values());
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableView.getModel());
        sorter.setRowFilter(andFilter);
        tableView.setRowSorter(sorter);
        History.instance.add(new HistoryEvent(this, sorter));
    }



    private RowFilter<Object, Object> getRowFilter(String text, Class<?> columnClass, int column){
        if (Number.class.isAssignableFrom(columnClass)){
            Number number = parseNumber(text);
            if (number == null) return null;

            return RowFilter.numberFilter(type, number, column);
        } else if (Date.class.isAssignableFrom(columnClass)) {
            Date date = parseDate(text);
            if (date == null) return null;

            return RowFilter.dateFilter(type, date, column);
        } else {
            return RowFilter.regexFilter(String.format(regex, text), column);
        }
    }

    private Number parseNumber(String text){
        try{
            return Integer.valueOf(text.trim());
        } catch (NumberFormatException e){
            String message = "error message";
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    private Date parseDate(String text) {
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        try {
            return df.parse(text.trim());
        } catch (ParseException e) {
            String message = "error message";
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return null;
    }
}
