package md.varoinform.view;

import md.varoinform.Settings;
import md.varoinform.util.ImageHelper;
import md.varoinform.util.ResourceBundleHelper;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/12/13
 * Time: 9:50 AM
 */
public class ToolbarButton extends JButton {
    private static final int iconHeight = 16;
    private static final int iconWidth = 16;
    private final String text;
    private final String toolTipText;

    private ToolbarButton(String filename, int width, int height, String text, String toolTipText) {
        this.text = text;
        this.toolTipText = toolTipText;
        ImageIcon icon = ImageHelper.getScaledImageIcon(filename, width, height);

        setIcon(icon);
        setFont(Settings.getDefaultFont("Serif", 14));
        setOpaque(false);
        setContentAreaFilled(true);
        updateDisplay();
    }

    public ToolbarButton(String filename, String toolTipText) {
        this(filename, iconWidth, iconHeight, "", toolTipText);
    }



    public ToolbarButton(String filename, String text, String toolTipText){
        this(filename, iconWidth, iconHeight, text, toolTipText);
    }

    public void updateDisplay(){
        setToolTipText(ResourceBundleHelper.getString(toolTipText, toolTipText));
        String text = ResourceBundleHelper.getString(this.text, this.text);
        setText(text);
    }

}
