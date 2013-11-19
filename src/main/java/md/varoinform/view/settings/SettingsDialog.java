package md.varoinform.view.settings;

import md.varoinform.controller.comparators.ColumnPriorityComparator;
import md.varoinform.controller.entityproxy.EnterpriseProxy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

import md.varoinform.controller.comparators.TranslateComparator;
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
    private List<Observer> observers = new ArrayList<>();
    private Set<String> columnNames = new TreeSet<>();

    public SettingsDialog(Component parent) {
        setSize(400, 400);
        setLocationRelativeTo(parent);
        setTitle(ResourceBundleHelper.getString("Settings", "Settings"));
        setLayout(new BorderLayout());

        JPanel columnsChoosePanel = createColumnChoosePanel();
        add(new JScrollPane(columnsChoosePanel));

        setModal(true);
    }

    private JPanel createColumnChoosePanel() {
        JPanel columnsChoosePanel = new JPanel();

        List<String> names = EnterpriseProxy.getViewPartNames();
        Collections.sort(names, new TranslateComparator());
        String prefColumns = preferencesHelper.getPrefColumns();

        columnsChoosePanel.setLayout(new GridLayout(names.size(), 1));
        for (String name : names) {
            boolean contains = prefColumns.contains(name);
            ColumnCheckBox checkBox = new ColumnCheckBox(name, contains);
            checkBox.addActionListener(new CheckBoxListener());
            columnsChoosePanel.add(checkBox);

            if (contains){
                columnNames.add(name);
            }
        }
        return columnsChoosePanel;
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


    private class CheckBoxListener extends AbstractAction{
        @Override
        public void actionPerformed(ActionEvent e) {
            ColumnCheckBox checkBox = (ColumnCheckBox) e.getSource();
            if ( checkBox.isSelected() ) {
                columnNames.add(checkBox.getName());
            } else {
                columnNames.remove(checkBox.getName());
            }
            setPrefColumns();
            notifyObservers(new ObservableEvent(ObservableEvent.STRUCTURE_CHANGED));
        }

        private void setPrefColumns() {
            StringBuilder buf = new StringBuilder();
            String delimiter = "";
            List<String> names = new ArrayList<>(columnNames);
            Collections.sort(names, new ColumnPriorityComparator());

            for (String name : names) {
                buf.append(delimiter);
                buf.append(name);
                delimiter = ";";
            }

            preferencesHelper.putPrefColumns(buf.toString());
        }
    }

}
