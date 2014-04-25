package md.varoinform.view;

import md.varoinform.util.ImageHelper;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/12/13
 * Time: 9:50 AM
 */
public class ToolbarButton extends JButton {
    private static final int iconHeight = 32;
    private static final int iconWidth = 32;

    public ToolbarButton(String filename, int width, int height) {
        ImageIcon icon = ImageHelper.getScaledImageIcon(filename, width, height);

        setIcon(icon);
        setPreferredSize(new Dimension(width + 4, height + 4));
        setOpaque(false);
        setContentAreaFilled(true);
    }

    public ToolbarButton(String filename) {
        this(filename, iconWidth, iconHeight);
    }

    public ToolbarButton(String filename, boolean enabled) {
        this(filename, iconWidth, iconHeight);
        setEnabled(enabled);
    }

    public ToolbarButton(String text, String filename){
        this(filename, iconWidth, iconHeight);
        FontMetrics metrics = getFontMetrics(getFont());
        int textWidth = metrics.stringWidth(text);
        setPreferredSize(new Dimension(textWidth + iconWidth + 20, iconHeight + 4));
    }
}
