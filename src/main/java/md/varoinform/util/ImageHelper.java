package md.varoinform.util;

import md.varoinform.Settings;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        Path path = Paths.get(Settings.getWorkFolder(), filename);
        return new ImageIcon(path.toString());
    }
}