package md.varoinform.view.dialogs.registration;

import md.varoinform.controller.DefaultLanguages;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.util.observer.ObservableEvent;
import md.varoinform.util.observer.Observer;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/18/13
 * Time: 2:12 PM
 */
public abstract class CardPanel extends JPanel implements Observer {

    protected final JEditorPane label = new JEditorPane();
    private String labelKey;
    protected DefaultLanguages language = DefaultLanguages.EN;

    protected CardPanel() {
        label.setEditable(false);
        label.setContentType("text/html");
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
    }

    public CardPanel(String labelKey) {
        this();
        this.labelKey = labelKey;
    }

    public abstract boolean isInputValid();


    @Override
    public void update(ObservableEvent event) {
        if (event.getType() == ObservableEvent.Type.LANGUAGE_CHANGED && event.getValue() instanceof DefaultLanguages) {
            language = (DefaultLanguages) event.getValue();
            updateDisplay();
        }

    }

    protected void updateDisplay(){
        String text = ResourceBundleHelper.getString(language, labelKey, "");
        label.setText(text);
    }
}
