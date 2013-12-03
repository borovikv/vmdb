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
    private final Set<String> selectedFields = new TreeSet<>();
    private final List<ColumnCheckBox> checkBoxes = new ArrayList<>();
    private CheckBoxSelectionPerformer checkBoxPerformer;

    public FieldChoosePanel() {
        List<String> fields = EnterpriseProxy.getFields();
        Collections.sort(fields, new TranslateComparator());

        PreferencesHelper preferencesHelper = new PreferencesHelper();
        String userFields = preferencesHelper.getUserFields();

        setLayout(new GridLayout(fields.size(), 1));
        for (String field : fields) {
            boolean isSelected = userFields.contains(field);
            ColumnCheckBox checkBox = createCheckBox(field, isSelected);

            add(checkBox);
            checkBoxes.add(checkBox);
            if (isSelected) {
                selectedFields.add(field);
            }
        }

    }

    private ColumnCheckBox createCheckBox(String field, boolean selected) {
        ColumnCheckBox checkBox = new ColumnCheckBox(field);
        checkBox.setSelected(selected);
        checkBox.addActionListener(new CheckBoxListener());
        return checkBox;
    }

    public void addCheckBoxGroupStateExecutor(CheckBoxSelectionPerformer executor){
         this.checkBoxPerformer = executor;
    }

    public List<String> getSelectedFieldNames() {
        return new ArrayList<>(selectedFields);
    }

    private class CheckBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ColumnCheckBox checkBox = (ColumnCheckBox) e.getSource();
            if ( checkBox.isSelected() ) {
                selectedFields.add(checkBox.getName());
            } else {
                selectedFields.remove(checkBox.getName());
            }

            if (checkBoxPerformer != null){
                checkBoxPerformer.perform(getSelectedFieldNames());
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