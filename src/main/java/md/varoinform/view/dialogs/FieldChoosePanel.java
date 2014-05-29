package md.varoinform.view.dialogs;

import md.varoinform.view.I18nCheckBox;
import md.varoinform.view.fieldgroup.FieldGroup;

import javax.swing.*;
import java.awt.GridLayout;
import java.util.*;

public class FieldChoosePanel extends JPanel {
    private final FieldGroup fieldGroup = new FieldGroup();

    public FieldChoosePanel() {
        List<I18nCheckBox> group = fieldGroup.getGroup();

        setLayout(new GridLayout(group.size(), 1));
        for (I18nCheckBox checkBox : group) {
            add(checkBox);
        }

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