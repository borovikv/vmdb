package md.varoinform.view.status;

import md.varoinform.util.ResourceBundleHelper;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 6/13/14
 * Time: 4:46 PM
 */
public class OutputLabel extends JLabel {
    private final String messageTotal = "total_rows";
    private final String messageRow = "current_row";
    private int total;
    private int row;
    private final String formatTotalOnly;
    private final String formatBoth;

    public OutputLabel() {
        formatTotalOnly = "%s: %d";
        formatBoth = "%s: %d | %s: %d";
        row = 0;
    }

    public void setTotal(int total){
        this.total = total;
        updateDisplay();
    }

    public void setRow(int row){
        this.row = row;
        updateDisplay();
    }

    public void updateDisplay(){
        String t;
        String i18nMessageTotal = ResourceBundleHelper.getString(messageTotal, messageTotal);
        String i18nMessageRow = ResourceBundleHelper.getString(messageRow, messageRow);
        if (row > 0){
            t = String.format(formatBoth, i18nMessageRow, row, i18nMessageTotal, total);
        } else {
            t = String.format(formatTotalOnly, i18nMessageTotal, total);
        }
        setText(t);
    }
}
