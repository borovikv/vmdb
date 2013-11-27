package md.varoinform.view.dialogs;

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
        String trText = ResourceBundleHelper.getString(text, text);
        setText(trText);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
