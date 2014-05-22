package md.varoinform.view.dialogs.print;

import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.view.dialogs.progress.Activity;

import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 5/22/14
 * Time: 1:33 PM
 */
public class PrintActivity extends Activity {
    private PrinterJob job;
    private PrintRequestAttributeSet attributes;

    public PrintActivity(PrinterJob job, PrintRequestAttributeSet attributes) {
        this.job = job;
        this.attributes = attributes;
    }

    @Override
    protected Void doInBackground() throws Exception {
        try {
            job.print(attributes);
            JOptionPane.showMessageDialog(null, ResourceBundleHelper.getString("printing_done"));
        } catch (PrinterException e1) {
            e1.printStackTrace();
        }
        return null;
    }
}
