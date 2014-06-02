package md.varoinform.view.demonstrator;

import md.varoinform.Settings;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 5/12/14
 * Time: 5:56 PM
 */
public class DataCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        if( value instanceof Date) {
            value = Settings.getDefaultDateFormat().format(value);
        }
        return super.getTableCellRendererComponent(table, value, isSelected,
                hasFocus, row, column);
    }
}
