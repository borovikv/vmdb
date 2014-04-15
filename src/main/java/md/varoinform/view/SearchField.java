package md.varoinform.view;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/15/14
 * Time: 10:17 AM
 */
public class SearchField extends JTextField {
    private String placeholder = "Поиск";
    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);

        if (getText().isEmpty() /*&& !(FocusManager.getCurrentKeyboardFocusManager().getFocusOwner() == this)*/) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setBackground(Color.gray);
            g2.setFont(getFont().deriveFont(Font.ITALIC));
            FontMetrics fontMetrics = g2.getFontMetrics();
            int height = fontMetrics.getHeight();
            g2.drawString(placeholder, 5, height); //figure out x, y from font's FontMetrics and size of component.
            g2.dispose();
        }
    }

    public void updateDisplay(){

    }
}
