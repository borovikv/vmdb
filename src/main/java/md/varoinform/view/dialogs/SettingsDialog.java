package md.varoinform.view.dialogs;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import md.varoinform.util.*;
import md.varoinform.util.Observable;
import md.varoinform.util.Observer;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/18/13
 * Time: 2:28 PM
 */
public class SettingsDialog extends JDialog implements Observable {
    private List<Observer> observers = new ArrayList<>();

    public SettingsDialog() {
        setSize(400, 450);
        setLocationRelativeTo(null);
        setTitle(ResourceBundleHelper.getString("Settings", "Settings"));
        setLayout(new BorderLayout());


        setModal(true);
    }



    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers(ObservableEvent event){
        for (Observer observer : observers) {
            observer.update(event);
        }
    }

    public void updateDisplay() {

    }




}
