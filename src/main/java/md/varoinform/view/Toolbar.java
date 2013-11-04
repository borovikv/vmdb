package md.varoinform.view;

import md.varoinform.util.ButtonHelper;

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

        addButton("/icons/home.png");
        addSeparator();
        addButton("/icons/arrow_left2.png");
        addButton("/icons/arrow_right2.png");
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
