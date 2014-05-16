package md.varoinform.view.status;

import md.varoinform.view.LanguageComboBox;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 5/16/14
 * Time: 2:52 PM
 */
public enum StatusBar {
    instance;

    private JPanel statusBar;

    StatusBar() {
        statusBar = new JPanel();
        statusBar.setLayout(new BorderLayout());
        statusBar.add(OutputLabel.instance.getLabel(), BorderLayout.WEST);

        LanguageComboBox languageCombo = new LanguageComboBox();
        statusBar.add(languageCombo, BorderLayout.EAST);
    }

    public Component getStatusBar() {
        return statusBar;
    }

}
