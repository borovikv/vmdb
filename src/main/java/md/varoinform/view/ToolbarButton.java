package md.varoinform.view;

import md.varoinform.Settings;
import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.entities.Language;
import md.varoinform.util.ImageHelper;
import md.varoinform.util.ResourceBundleHelper;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.FontMetrics;

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
        setPreferredSize(new Dimension(width + 4, height + 4));
        setOpaque(false);
        setContentAreaFilled(true);
        updateDisplay();
    }

    public ToolbarButton(String filename, String toolTipText) {
        this(filename, iconWidth * 2, iconHeight * 2, "", toolTipText);
    }



    public ToolbarButton(String filename, String text, String toolTipText){
        this(filename, iconWidth, iconHeight, text, toolTipText);
        int textWidth = getTextWidth();
        int left = getBorder().getBorderInsets(this).left;
        int right = getBorder().getBorderInsets(this).right;
        setPreferredSize(new Dimension(textWidth + iconWidth + left + right, iconHeight * 2 + 4));
    }

    private int getTextWidth() {
        FontMetrics metrics = getFontMetrics(getFont());
        java.util.List<Language> languages = LanguageProxy.instance.getLanguages();
        int result = 0;
        for (Language language : languages) {
            String text = ResourceBundleHelper.getString(language, this.text, this.text);
            int width = metrics.stringWidth(text);
            if (width > result) result = width;
        }
        return result;
    }

    public void updateDisplay(){
        setToolTipText(ResourceBundleHelper.getString(toolTipText, toolTipText));
        String text = ResourceBundleHelper.getString(this.text, this.text);
        setText(text);
    }

}
