package md.varoinform.view;

import md.varoinform.controller.*;
import md.varoinform.controller.enterprisecomparators.DefaultComparator;
import md.varoinform.controller.enterprisecomparators.TitleComparator;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.util.AbstractProxyListener;
import md.varoinform.util.ButtonHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/30/13
 * Time: 10:45 AM
 */
public class Toolbar extends JToolBar implements Observer {
    private final Demonstrator demonstrator;
    private JButton homeButton;
    private JButton backButton;
    private JButton forwardButton;
    private JButton printButton;
    private JButton exportButton;
    private JButton mailButton;
    private JButton settingsButton;
    private JTextField textField;
    private JComboBox comboBox;

    private SearchProxy searchProxy;
    private HistoryProxy historyProxy;

    public Toolbar(Demonstrator demonstrator) {
        //setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.demonstrator = demonstrator;
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

        List<Comparator<Enterprise>> comparators = new ArrayList<>();
        comparators.add(new DefaultComparator());
        comparators.add(new TitleComparator());
        comboBox = new JComboBox<>(comparators.toArray());
        comboBox.setEnabled(false);
        setSorting();

        createToolbar();

        searchProxy = new SearchProxy(demonstrator);
        setSearchProxy(searchProxy);

        updateDisplay();
    }

    public void setHistoryProxy(Proxy<String> proxy){
        historyProxy = (HistoryProxy)proxy;
        homeButton.addActionListener(new HistoryAction(proxy, "home"));
        backButton.addActionListener(new HistoryAction(proxy, "back"));
        forwardButton.addActionListener(new HistoryAction(proxy, "forward"));
        homeButton.setEnabled(true);
    }

    public void setSearchProxy(Proxy<String> proxy){
        textField.addActionListener(new SearchAction(proxy));
        textField.setEnabled(true);
    }

    public void setSorting(){
        comboBox.addItemListener( new ItemListener() {
            private List<Enterprise> enterprises;
            @Override
            public void itemStateChanged(ItemEvent e) {
                if ( e.getStateChange() == ItemEvent.DESELECTED && e.getItem() instanceof DefaultComparator ){
                    enterprises = new ArrayList<>(demonstrator.getALL());

                } else if ( e.getStateChange() == ItemEvent.SELECTED && e.getItem() instanceof DefaultComparator ) {
                    demonstrator.showResults(enterprises);

                } else if ( e.getStateChange() == ItemEvent.SELECTED ) {
                    Comparator<Enterprise> comparator = (Comparator<Enterprise>)comboBox.getSelectedItem();
                    List<Enterprise> enterprises = demonstrator.getALL();
                    Collections.sort(enterprises, comparator);
                    demonstrator.showResults(enterprises);

                }
            }
        } );
        comboBox.setEnabled(true);
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

    @Override
    public void update(Observable o, Object arg) {
        ObservableEvent event = (ObservableEvent) arg;

        switch (event.getType()){

            case ObservableEvent.HISTORY_MOVE:
                if ( event.getValue() instanceof String || event.getValue() == null ){
                    String value = (String) event.getValue();
                    textField.setText(value);
                    searchProxy.perform(value);
                }
                break;

            case ObservableEvent.BACK_SET_ENABLE:
                backButton.setEnabled((boolean)event.getValue());
                break;

            case ObservableEvent.FORWARD_SET_ENABLE:
                forwardButton.setEnabled((boolean)event.getValue());
                break;

            case ObservableEvent.LANGUAGE_CHANGED:
                comboBox.updateUI();
                updateDisplay();
                break;
        }

    }

    private void updateDisplay() {
        Locale locale = new Locale(LanguageProxy.getInstance().getCurrentLanguage().getTitle());
        ResourceBundle bundle = ResourceBundle.getBundle("i18n.Strings", locale);
        homeButton.setToolTipText(bundle.getString("home"));
        backButton.setToolTipText(bundle.getString("back"));
        forwardButton.setToolTipText(bundle.getString("forward"));
        comboBox.setToolTipText(bundle.getString("orderBy"));
        mailButton.setToolTipText(bundle.getString("mail"));
        exportButton.setToolTipText(bundle.getString("export"));
        printButton.setToolTipText(bundle.getString("print"));
        settingsButton.setToolTipText(bundle.getString("settings"));
    }


    private class SearchAction extends AbstractProxyListener<String> {
        public SearchAction(Proxy<String> proxy) {
            super(proxy);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String value = e.getActionCommand();
            proxy.perform(value);
            historyProxy.append(value);
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
        }
    }
}
