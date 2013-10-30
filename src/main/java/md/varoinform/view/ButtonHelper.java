package md.varoinform.view;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class ButtonHelper implements Serializable {
    public ButtonHelper() {
    }

    public  static JButton createButton(String filename, int width, int height) {
        JButton button = new JButton();
        ImageIcon icon = getScaledImageIcon(filename, width, height);

        button.setIcon(icon);
        button.setPreferredSize(new Dimension(width + 4, height + 4));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        //button.setBorderPainted(false);
        return button;
    }

    private static ImageIcon getScaledImageIcon(String filename, int width, int height) {
        ImageIcon icon = getImageIcon(filename);
        if (icon != null) {
            Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            icon = new ImageIcon(image);
        }
        return icon;
    }

    private static ImageIcon getImageIcon(String filename) {
        ImageIcon icon = null;
        try {
            icon = new ImageIcon(ButtonHelper.class.getResource(filename));
        } catch (Exception e) {
        }
        return icon;
    }
}