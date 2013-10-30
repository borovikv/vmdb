package md.varoinform.view;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/30/13
 * Time: 11:18 AM
 */
public class StatusBar extends JPanel{
    public StatusBar() {
        setBorder(BorderFactory.createEtchedBorder());
        JLabel outputLabel = new JLabel();
        outputLabel.setText("result: 0");
        add(outputLabel);
    }
}
