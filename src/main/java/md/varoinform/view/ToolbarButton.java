package md.varoinform.view;

import md.varoinform.Settings;
import md.varoinform.util.ImageHelper;
import md.varoinform.util.PreferencesHelper;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/12/13
 * Time: 9:50 AM
 */
public class ToolbarButton extends I18nButton {
    private static final int iconHeight = 16;
    private static final int iconWidth = 16;

    private ToolbarButton(String filename, int width, int height, String text, String toolTipText) {
        super(text, toolTipText);
        ImageIcon icon = ImageHelper.getScaledImageIcon(filename, width, height);

        setIcon(icon);
        setFont(Settings.getDefaultFont(Settings.Fonts.SANS_SERIF, 14));
        //setOpaque(false);
        //setContentAreaFilled(false);
        updateDisplay();
    }

    public ToolbarButton(String filename, String toolTipText) {
        this(filename, iconWidth, iconHeight, "", toolTipText);
    }



    public ToolbarButton(String filename, String text, String toolTipText){
        this(filename, iconWidth, iconHeight, text, toolTipText);
    }

    public static int getMinWith() {
        return iconWidth;
    }

    @Override
    public void updateDisplay(){
        super.updateDisplay();
        PreferencesHelper helper = new PreferencesHelper();
        if (!helper.getShowTextInButton()){
            setText("");
        }
    }
}
