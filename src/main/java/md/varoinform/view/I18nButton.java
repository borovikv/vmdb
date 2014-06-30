package md.varoinform.view;

import md.varoinform.controller.DefaultLanguages;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.util.observer.ObservableEvent;
import md.varoinform.util.observer.Observer;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 6/30/14
 * Time: 11:13 AM
 */
public class I18nButton extends JButton implements Observer {
    protected String text;
    protected String toolTipText;

    public I18nButton(String text, String toolTipText) {
        this.text = text;
        this.toolTipText = toolTipText;
    }

    @Override
    public void update(ObservableEvent event) {
        if (event.getType() == ObservableEvent.Type.LANGUAGE_CHANGED){
            Object value = event.getValue();
            if (value instanceof DefaultLanguages){
                updateDisplay((DefaultLanguages)value);
            } else {
                updateDisplay();
            }
        }

    }

    public void updateDisplay(){
        updateDisplay(null);
    }

    public void updateDisplay(DefaultLanguages language){
        if (toolTipText != null && !toolTipText.isEmpty()) {
            setToolTipText(getString(language, toolTipText, toolTipText));
        }

        if (text != null && !text.isEmpty()) {
            setText(getString(language, this.text, this.text));
        }
    }

    public String getString(DefaultLanguages language, String key, String defValue) {
        if (language != null) {
            return ResourceBundleHelper.getString(language, key, defValue);
        }
        return ResourceBundleHelper.getString(key, defValue);
    }
}
