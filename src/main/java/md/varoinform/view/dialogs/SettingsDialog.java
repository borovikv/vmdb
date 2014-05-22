package md.varoinform.view.dialogs;

import md.varoinform.util.ResourceBundleHelper;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/18/13
 * Time: 2:28 PM
 */
public class SettingsDialog extends JDialog {

    public SettingsDialog() {
        setSize(400, 450);
        setLocationRelativeTo(null);
        setTitle(ResourceBundleHelper.getString("Settings", "Settings"));
        setLayout(new BorderLayout());


        setModal(true);
    }



    public void updateDisplay() {

    }




}
