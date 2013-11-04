package md.varoinform.util;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class ButtonHelper implements Serializable {
    private static final int width = 36;
    private static final int height = 36;
    private ButtonHelper() {
    }

    public  static JButton createButton(String filename, int width, int height) {
        JButton button = new JButton();
        ImageIcon icon = ImageHelper.getScaledImageIcon(filename, width, height);

        button.setIcon(icon);
        button.setPreferredSize(new Dimension(width + 4, height + 4));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        //button.setBorderPainted(false);
        return button;
    }

    public static JButton createButton(String filename){
        return ButtonHelper.createButton(filename, width, height);
    }

    public static JButton createButton(String filename, boolean enable){
        JButton button = createButton(filename);
        button.setEnabled(enable);
        return button;
    }

}