package md.varoinform.view.dialogs;

import md.varoinform.Settings;
import md.varoinform.controller.LanguageProxy;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.view.demonstrator.Browser;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 7/28/14
 * Time: 4:26 PM
 */
public class AboutDialog extends JDialog {
    public AboutDialog() {
        setModal(true);
        setSize(400, 400);
        setTitle(ResourceBundleHelper.getString("about", "About"));
        setIconImage(Settings.getMainIcon());
        setLocationRelativeTo(null);

        JEditorPane pane = new Browser();
        Path path = Paths.get(Settings.getWorkFolder(), "external-resources", "about", String.format("help_%s.html", LanguageProxy.getCurrentLanguageTitle().substring(0, 2)));
        try {
            String s = FileUtils.readFileToString(path.toFile());
            pane.setText(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        add(pane);
    }

    public static void showDialog(){
        AboutDialog dialog = new AboutDialog();
        dialog.setVisible(true);
        dialog.dispose();
    }
}
