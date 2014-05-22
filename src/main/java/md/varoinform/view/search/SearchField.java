package md.varoinform.view.search;

import md.varoinform.Settings;
import md.varoinform.util.ResourceBundleHelper;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/15/14
 * Time: 10:17 AM
 */
public class SearchField extends JTextField {
    public SearchField() {
        setFont(Settings.getDefaultFont("SANS_SERIF"));
    }

    public int height() {
        return getFontMetrics(getFont()).getHeight() + 10;
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);

        if (getText().isEmpty()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setBackground(Color.gray);
            g2.setFont(getFont().deriveFont(Font.ITALIC));
            FontMetrics fontMetrics = g2.getFontMetrics();
            int height = fontMetrics.getHeight();
            g2.drawString(getPlaceholder(), 5, height); //figure out x, y from font's FontMetrics and size of component.
            g2.dispose();
        }
    }

    private String getPlaceholder() {
        return ResourceBundleHelper.getString("search");
    }

    public void updateDisplay(){
        repaint();
    }
}
