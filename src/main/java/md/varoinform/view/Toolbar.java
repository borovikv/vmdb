package md.varoinform.view;

import md.varoinform.controller.Demonstrator;
import md.varoinform.controller.HistoryProxy;
import md.varoinform.controller.Proxy;
import md.varoinform.controller.SearchProxy;
import md.varoinform.util.AbstractProxyListener;
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
    private HistoryProxy historyProxy;
    private SearchProxy searchProxy;

    private String[] items = {
            "by relevant",
            "by name"
    };

    public Toolbar(Demonstrator demonstrator) {
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

        historyProxy = new HistoryProxy(demonstrator);
        searchProxy = new SearchProxy(demonstrator, historyProxy);

        createToolbar();

        setHistoryProxy(historyProxy);
        setSearchProxy(searchProxy);
    }

    public void setHistoryProxy(Proxy<String> proxy){
        homeButton.addActionListener(new HistoryAction(proxy, "home"));
        backButton.addActionListener(new HistoryAction(proxy, "back"));
        forwardButton.addActionListener(new HistoryAction(proxy, "forward"));
        homeButton.setEnabled(true);
        //backButton.setEnabled(true);
        //forwardButton.setEnabled(true);

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


    private class SearchAction extends AbstractProxyListener<String> {
        public SearchAction(Proxy<String> proxy) {
            super(proxy);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            proxy.perform(e.getActionCommand());
            backButton.setEnabled(true);
        }
    }

    private class HistoryAction extends AbstractProxyListener<String> {
        String command;
        public HistoryAction(Proxy<String> proxy, String command) {
            super(proxy);
            this.command = command;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            proxy.perform(command);
            forwardButton.setEnabled(((HistoryProxy)proxy).hasForward());
            backButton.setEnabled(((HistoryProxy)proxy).hasBack());
        }
    }
}
