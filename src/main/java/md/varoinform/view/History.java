package md.varoinform.view;

import md.varoinform.controller.HistoryProxy;
import md.varoinform.util.Observable;
import md.varoinform.util.ObservableEvent;
import md.varoinform.util.Observer;
import md.varoinform.util.ResourceBundleHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class History implements Observable {
    private final JButton homeButton = new ToolbarButton("/external-resources/icons/home.png", "home");
    //private final JButton backButton = new ToolbarButton("/external-resources/icons/arrow_left2.png", false);
    //private final JButton forwardButton = new ToolbarButton("/external-resources/icons/arrow_right2.png", false);
    private final HistoryProxy historyProxy = new HistoryProxy();

    // back
    private final AbstractAction backAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object obj = historyProxy.back();
            notifyObservers(new ObservableEvent(ObservableEvent.BACK, obj));
            //backButton.setEnabled(historyProxy.hasBack());
            //forwardButton.setEnabled(true);
        }
    };

    // forward
    private final AbstractAction forwardAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object obj = historyProxy.forward();
            notifyObservers(new ObservableEvent(ObservableEvent.FORWARD, obj));
            //forwardButton.setEnabled(historyProxy.hasForward());
            //backButton.setEnabled(true);
        }
    };

    // home
    private final AbstractAction homeAction  = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            historyProxy.home();
            notifyObservers(new ObservableEvent(ObservableEvent.HOME, null));
            //forwardButton.setEnabled(false);
           // backButton.setEnabled(historyProxy.hasBack());
        }
    };
    private List<Observer> observers = new ArrayList<>();


    public History() {
        homeButton.addActionListener(homeAction);
        //backButton.addActionListener(backAction);
        //forwardButton.addActionListener(forwardAction);

    }

    public void appendHistory(Object value) {
        historyProxy.append(value);
        //forwardButton.setEnabled(false);
        //backButton.setEnabled(true);
    }

    public void updateDisplay() {
        //backButton.setToolTipText(ResourceBundleHelper.getString("back"));
        //forwardButton.setToolTipText(ResourceBundleHelper.getString("forward"));
        homeButton.setToolTipText(ResourceBundleHelper.getString("home"));
    }

    /*public JButton getBackButton() {
        return backButton;
    }

    public JButton getForwardButton() {
        return forwardButton;
    }
    */
    public JButton getHomeButton() {
        return homeButton;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers(ObservableEvent event) {
        for (Observer observer : observers) {
            observer.update(event);
        }
    }
}