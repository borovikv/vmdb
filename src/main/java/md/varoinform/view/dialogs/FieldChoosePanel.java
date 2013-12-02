package md.varoinform.view.dialogs;

import md.varoinform.controller.comparators.TranslateComparator;
import md.varoinform.controller.entityproxy.EnterpriseProxy;
import md.varoinform.util.PreferencesHelper;

import javax.swing.*;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class FieldChoosePanel extends JPanel {
    private final PreferencesHelper preferencesHelper = new PreferencesHelper();
    private final Set<String> fieldNames = new TreeSet<>();
    private final List<ColumnCheckBox> checkBoxes = new ArrayList<>();
    private Executor executor;

    public FieldChoosePanel() {
        List<String> names = EnterpriseProxy.getViewPartNames();
        Collections.sort(names, new TranslateComparator());
        String prefColumns = preferencesHelper.getPrefColumns();

        setLayout(new GridLayout(names.size(), 1));
        for (String name : names) {
            boolean contains = prefColumns.contains(name);

            ColumnCheckBox checkBox = new ColumnCheckBox(name);
            checkBox.setSelected(contains);
            checkBox.addActionListener(new CheckBoxListener());
            add(checkBox);
            checkBoxes.add(checkBox);

            if (contains) {
                fieldNames.add(name);
            }
        }

    }

    public void addCheckBoxGroupStateExecutor(Executor executor){
         this.executor = executor;
    }

    public List<String> getSelectedFieldNames() {
        return new ArrayList<>(fieldNames);
    }

    private class CheckBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ColumnCheckBox checkBox = (ColumnCheckBox) e.getSource();
            if ( checkBox.isSelected() ) {
                fieldNames.add(checkBox.getName());
            } else {
                fieldNames.remove(checkBox.getName());
            }

            if (executor != null){
                executor.execute(getSelectedFieldNames());
            }
        }
    }

    public void setEnable(boolean enable){
        for (ColumnCheckBox checkBox : checkBoxes) {
            checkBox.setEnabled(enable);
        }
    }

    public void updateDisplay(){
        for (ColumnCheckBox checkBox : checkBoxes) {
            checkBox.updateDisplay();
        }
    }
}