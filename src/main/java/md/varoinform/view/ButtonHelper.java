package md.varoinform.view;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class ButtonHelper implements Serializable {
    public ButtonHelper() {
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

}