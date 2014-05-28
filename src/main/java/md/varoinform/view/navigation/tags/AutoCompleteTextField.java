package md.varoinform.view.navigation.tags;

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

    public AutoCompleteTextField(String placeholder) {
        this.placeholder = placeholder;
        Font font = new Font("Serif", Font.PLAIN, 14);
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
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
            g2.drawString(ResourceBundleHelper.getString(placeholder), 5, height);
            g2.dispose();
        }
    }

    public void updateDisplay(){
        repaint();
    }

}
