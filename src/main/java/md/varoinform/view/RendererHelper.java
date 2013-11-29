package md.varoinform.view;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/29/13
 * Time: 1:20 PM
 */
public class RendererHelper {
    protected static final Color HIGHLIGHT_COLOR;
    static {
        ResourceBundle bundle = ResourceBundle.getBundle("VaroDB");
        HIGHLIGHT_COLOR = (Color)bundle.getObject("highlightColor");
    }
    private static Color background;

    public static JPanel getPanel(String title){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(background);

        JLabel label = new JLabel(title);

        panel.add(label);
        int height = 6 + label.getFontMetrics(label.getFont()).getHeight();
        int width = panel.getPreferredSize().width;
        panel.setPreferredSize(new Dimension(width, height));

        return panel;

    }

    public void setBackground(boolean sel, Color defBackground) {
        background = sel ? HIGHLIGHT_COLOR: defBackground;
    }

    public static Color getBackground() {
        return background;
    }
}
