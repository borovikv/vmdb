package md.varoinform.view.status;

import md.varoinform.util.ResourceBundleHelper;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/12/13
 * Time: 12:27 PM
 */
public enum OutputLabel {
    instance;

    private JLabel label = new JLabel();
    private String message;
    private int resultCount;
    public Component getLabel(){
        return label;
    }


    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
        label.setText(message + ": " + resultCount);
    }

    public void updateDisplay(){
        this.message = ResourceBundleHelper.getString("result", "result");
        setResultCount(resultCount);
    }

}
