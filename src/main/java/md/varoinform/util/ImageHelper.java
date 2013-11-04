package md.varoinform.util;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class ImageHelper implements Serializable {
    public static ImageIcon getScaledImageIcon(String filename, int width, int height) {
        ImageIcon icon = getImageIcon(filename);
        if (icon != null) {
            Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            icon = new ImageIcon(image);
        }
        return icon;
    }

    public static ImageIcon getImageIcon(String filename) {
        ImageIcon icon = null;
        try {
            icon = new ImageIcon(ButtonHelper.class.getResource(filename));
        } catch (Exception e) {
        }
        return icon;
    }
}