package md.varoinform.view.fieldgroup;

import md.varoinform.util.ResourceBundleHelper;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/18/13
 * Time: 3:31 PM
 */
public class ColumnCheckBox extends JCheckBox{
    private String name;

    public ColumnCheckBox(String text) {
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
