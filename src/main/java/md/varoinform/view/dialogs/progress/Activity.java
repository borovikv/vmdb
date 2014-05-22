package md.varoinform.view.dialogs.progress;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 5/17/14
 * Time: 10:32 AM
 */
public abstract class Activity extends SwingWorker<Void, Integer> {
    protected String note = "";
    protected final int millis = 10;

    public String getNote() {return note;}

    public void setNote(int i, int size) {
        String format = getFormat();
        if (format == null || format.isEmpty()) return;
        this.note = String.format(format, i, size);
    }

    public String getFormat() {
        return "%d from %d";
    }

}
