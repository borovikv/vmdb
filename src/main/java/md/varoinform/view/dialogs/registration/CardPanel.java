package md.varoinform.view.dialogs.registration;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/18/13
 * Time: 2:12 PM
 */
public abstract class CardPanel extends JPanel {
    protected final JTextArea label = new JTextArea("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor " +
            "incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris " +
            "nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum " +
            "dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia " +
            "deserunt mollit anim id est laborum.");

    public CardPanel() {
        label.setEditable(false);
        label.setLineWrap(true);
        label.setWrapStyleWord(true);
        label.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        setLabelText();
    }

    public abstract boolean isInputValid();
    protected abstract void setLabelText();
}
