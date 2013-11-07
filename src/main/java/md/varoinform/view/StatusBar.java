package md.varoinform.view;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/30/13
 * Time: 11:18 AM
 */
public class StatusBar extends JPanel{
    private JLabel outputLabel;

    public StatusBar() {
        setBorder(BorderFactory.createEtchedBorder());
        setLayout(new BorderLayout());
        outputLabel = new JLabel();
        outputLabel.setText("result: 0");
        add(outputLabel, BorderLayout.WEST);
        addComboBox();
    }

    private void addComboBox() {
        String[] items = {
                "ru",
                "en",
                "ro"
        };
        JComboBox comboBox = new JComboBox(items);
        add(comboBox, BorderLayout.EAST);
    }

    public void setResult(int resultCount){
        outputLabel.setText("result: " + resultCount);
    }
}
