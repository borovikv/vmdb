package md.varoinform.view;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/30/13
 * Time: 10:45 AM
 */
public class Toolbar extends JToolBar{
    //private final ButtonHelper buttonHelper = new ButtonHelper();

    public Toolbar() {
        //setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setFloatable(false);
        //setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
        addSeparator();
        SearchPanel searchPanel = new SearchPanel();
        add(searchPanel, BorderLayout.CENTER);
        addSeparator();

        addComboBox();
        addSeparator();

        addButton("/icons/print.png");
        Dimension dimension = new Dimension(2, 0);
        addSeparator(dimension);
        addButton("/icons/export.png");
        addSeparator(dimension);
        addButton("/icons/mail.png");
        addSeparator(dimension);
        addButton("/icons/settings.png");
        addSeparator();
    }

    private void addComboBox() {
        String[] items = {
                "by relevant",
                "by name"
        };
        JComboBox comboBox = new JComboBox(items);
        add(comboBox);
    }

    private void addButton(String iconFileName) {
        int width = 36;
        int height = 36;
        JButton mailButton = ButtonHelper.createButton(iconFileName, width, height);
        add(mailButton);
    }


}
