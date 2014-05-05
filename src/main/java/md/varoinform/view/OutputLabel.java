package md.varoinform.view;

import md.varoinform.util.ResourceBundleHelper;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/12/13
 * Time: 12:27 PM
 */
public class OutputLabel extends JLabel {
    private String message;
    private int resultCount;

    public void setMessageText(String message) {
        this.message = message;
        setResultCount(resultCount);
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
        setText(message + ": " + resultCount);
    }

    public void updateDisplay(){
        setMessageText(ResourceBundleHelper.getString("result", "result"));
    }
}
