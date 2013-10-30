package md.varoinform.view;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/30/13
 * Time: 10:29 AM
 */
public class SearchPanel extends JPanel{
    public SearchPanel() {
        setLayout(new BorderLayout());
        JTextField textField = new JTextField();
        Font font = new Font(Font.SERIF, Font.PLAIN, 18);
        textField.setFont(font);
        textField.setPreferredSize(new Dimension(0, 36));
        add(textField);
        textField.setText("Hello world");
    }
}
