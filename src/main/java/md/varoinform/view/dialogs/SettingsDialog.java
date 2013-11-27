package md.varoinform.view.dialogs;

import md.varoinform.controller.comparators.ColumnPriorityComparator;

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
    private final PreferencesHelper preferencesHelper = new PreferencesHelper();
    private final FieldChoosePanel fieldChoosePanel = new FieldChoosePanel();
    private List<Observer> observers = new ArrayList<>();

    public SettingsDialog(Component parent) {
        setSize(400, 450);
        setLocationRelativeTo(parent);
        setTitle(ResourceBundleHelper.getString("Settings", "Settings"));
        setLayout(new BorderLayout());

        fieldChoosePanel.addCheckBoxGroupStateExecutor(new CheckBoxExecutor());
        add(new JScrollPane(fieldChoosePanel));

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


    private class CheckBoxExecutor implements Executor{
        @Override
        public void execute(List<String> names) {
            Collections.sort(names, new ColumnPriorityComparator());
            preferencesHelper.putPrefColumns(names);

            notifyObservers(new ObservableEvent(ObservableEvent.STRUCTURE_CHANGED));
        }
    }

}
