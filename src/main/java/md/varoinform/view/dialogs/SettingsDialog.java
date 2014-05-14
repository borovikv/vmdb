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

    public SettingsDialog() {
        setSize(400, 450);
        setLocationRelativeTo(null);
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

    public void updateDisplay() {
        fieldChoosePanel.updateDisplay();
    }


    private class CheckBoxExecutor implements CheckBoxSelectionPerformer {
        @Override
        public void perform(List<String> names) {
            Collections.sort(names, new ColumnPriorityComparator());
            preferencesHelper.putUserFields(names);

            notifyObservers(new ObservableEvent(ObservableEvent.Type.STRUCTURE_CHANGED));
        }
    }

}
