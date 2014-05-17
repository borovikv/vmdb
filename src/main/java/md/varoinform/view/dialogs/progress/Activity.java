package md.varoinform.view.dialogs.progress;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 5/17/14
 * Time: 10:32 AM
 */
public abstract class Activity extends SwingWorker<Void, Integer> {

    protected final int millis = 10;

    public String getNote() {return "";}

}
