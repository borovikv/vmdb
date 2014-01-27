package md.varoinform.view.dialogs.registration;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/18/13
 * Time: 2:12 PM
 */
public abstract class CardPanel extends JPanel implements Observer {

    protected final JTextArea label = new JTextArea();
    private String labelKey;


    public CardPanel(String labelKey) {
        this.labelKey = labelKey;

        label.setEditable(false);
        label.setLineWrap(true);
        label.setWrapStyleWord(true);
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        setLabelText(labelKey);
    }



    private void setLabelText(String key){
        String text = getText(key, "");
        label.setText(text);
    }

    public String getText(String key, String def) {
        return TranslateHelper.instance.getText(key, def);
    }

    public abstract boolean isInputValid();

    @Override
    public void update(Observable o, Object arg) {
        setLabelText(labelKey);
        updateDisplay();
    }

    protected abstract void updateDisplay();
}
