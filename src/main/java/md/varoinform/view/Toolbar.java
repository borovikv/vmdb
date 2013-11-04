package md.varoinform.view;

import md.varoinform.controller.Proxy;
import md.varoinform.util.ButtonHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/30/13
 * Time: 10:45 AM
 */
public class Toolbar extends JToolBar{
    private JButton homeButton;
    private JButton backButton;
    private JButton forwardButton;
    private JButton printButton;
    private JButton exportButton;
    private JButton mailButton;
    private JButton settingsButton;
    private JTextField textField;
    private JComboBox comboBox;

    private String[] items = {
            "by relevant",
            "by name"
    };

    public Toolbar() {
        //setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        setFloatable(false);

        homeButton = ButtonHelper.createButton("/icons/home.png", false);
        backButton = ButtonHelper.createButton("/icons/arrow_left2.png", false);
        forwardButton = ButtonHelper.createButton("/icons/arrow_right2.png", false);
        printButton = ButtonHelper.createButton("/icons/print.png", false);
        exportButton = ButtonHelper.createButton("/icons/export.png", false);
        mailButton = ButtonHelper.createButton("/icons/mail.png", false);
        settingsButton = ButtonHelper.createButton("/icons/settings.png", false);

        textField = new JTextField();
        textField.setFont(new Font(Font.SERIF, Font.PLAIN, 18));
        textField.setPreferredSize(new Dimension(0, 36));
        textField.setEnabled(false);

        comboBox = new JComboBox(items);
        comboBox.setEnabled(false);

        createToolbar();
    }

    public void setHistoryProxy(Proxy proxy){

    }

    public void setSearchProxy(Proxy<String> proxy){
        textField.addActionListener(new SearchAction(proxy));
        textField.setEnabled(true);
    }

    private void createToolbar() {
        add(homeButton);
        addSeparator();
        add(backButton);
        add(forwardButton);
        addSeparator();
        add(textField, BorderLayout.CENTER);
        addSeparator();
        add(comboBox);
        addSeparator();
        add(printButton);
        Dimension dimension = new Dimension(2, 0);
        addSeparator(dimension);
        add(exportButton);
        addSeparator(dimension);
        add(mailButton);
        addSeparator(dimension);
        add(settingsButton);
    }


    private static class SearchAction implements ActionListener {
        Proxy<String> proxy;

        public SearchAction(Proxy<String> proxy) {
            this.proxy = proxy;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            proxy.perform(e.getActionCommand());
        }
    }
}
