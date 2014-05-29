package md.varoinform.view.navigation;

import md.varoinform.Settings;
import md.varoinform.util.ResourceBundleHelper;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/29/13
 * Time: 10:01 AM
 */
public class AutoCompleteTextField extends JTextField {

    private final String placeholder;
    private final int padding;

    public AutoCompleteTextField(String placeholder) {
        this.placeholder = placeholder;
        Font font = Settings.getDefaultFont(Settings.Fonts.SANS_SERIF);
        padding = 5;
        setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        setFont(font);
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
            g2.drawString(ResourceBundleHelper.getString(placeholder), padding, height);
            g2.dispose();
        }
    }

    public void updateDisplay(){
        repaint();
    }

}
