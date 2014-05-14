package md.varoinform.view.dialogs;

import md.varoinform.view.fieldgroup.ColumnCheckBox;
import md.varoinform.view.fieldgroup.FieldGroup;

import javax.swing.*;
import java.awt.GridLayout;
import java.util.*;

public class FieldChoosePanel extends JPanel {
    private final FieldGroup fieldGroup = new FieldGroup();

    public FieldChoosePanel() {
        List<ColumnCheckBox> group = fieldGroup.getGroup();

        setLayout(new GridLayout(group.size(), 1));
        for (ColumnCheckBox checkBox : group) {
            add(checkBox);
        }

    }

    public void addCheckBoxGroupStateExecutor(CheckBoxSelectionPerformer executor){
         fieldGroup.addCheckBoxGroupStateExecutor(executor);
    }

    public List<String> getSelectedFieldNames() {
        return fieldGroup.getSelectedFieldNames();
    }


    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        fieldGroup.setEnable(enabled);
    }


    public void updateDisplay(){
        fieldGroup.updateDisplay();
    }
}