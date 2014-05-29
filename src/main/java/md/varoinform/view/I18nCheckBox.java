package md.varoinform.view;

import md.varoinform.util.ResourceBundleHelper;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/18/13
 * Time: 3:31 PM
 */
public class I18nCheckBox extends JCheckBox{
    private String name;

    public I18nCheckBox(String text) {
        super();
        this.name = text;
        updateDisplay();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void updateDisplay() {
        setText(getI18nName());
    }

    public String getI18nName() {
        return ResourceBundleHelper.getString(name, name);
    }
}
