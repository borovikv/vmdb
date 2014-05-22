package md.varoinform.view.fieldgroup;

import md.varoinform.controller.comparators.TranslateComparator;
import md.varoinform.controller.entityproxy.EnterpriseProxy;
import md.varoinform.util.PreferencesHelper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 5/14/14
 * Time: 10:31 AM
 */
public class FieldGroup {
    private final Set<String> selectedFields = new TreeSet<>();
    private final List<ColumnCheckBox> checkBoxes = new ArrayList<>();
    private CheckBoxSelectionPerformer checkBoxPerformer;
    private ColumnCheckBox selectAll = new ColumnCheckBox("select_all");

    public FieldGroup() {
        List<String> userFields = new PreferencesHelper().getUserFields();
        for (String field : EnterpriseProxy.getFields()) {
            boolean selected = userFields.contains(field);

            ColumnCheckBox checkBox = createCheckBox(field, selected);
            checkBoxes.add(checkBox);

            if (selected) {
                selectedFields.add(field);
            }
        }

        if (selectedFields.size() == checkBoxes.size()){
            selectAll.setSelected(true);
        }

        selectAll.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (selectAll.isSelected()) {
                    for (ColumnCheckBox checkBox : checkBoxes) {
                        checkBox.setSelected(true);
                        performSelect(checkBox);
                    }
                }
            }
        });
    }

    private ColumnCheckBox createCheckBox(String field, final boolean selected) {
        ColumnCheckBox checkBox = new ColumnCheckBox(field);
        checkBox.setSelected(selected);
        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectAll.setSelected(false);
                performSelect((ColumnCheckBox) e.getSource());
            }
        });
        return checkBox;
    }

    public void performSelect(ColumnCheckBox checkBox) {
        if ( checkBox.isSelected() ) {
            selectedFields.add(checkBox.getName());
        } else {
            selectedFields.remove(checkBox.getName());
        }

        if (checkBoxPerformer != null){
            checkBoxPerformer.perform(getSelectedFieldNames());
        }
    }

    public List<String> getSelectedFieldNames() {
        return new ArrayList<>(selectedFields);
    }

    public List<ColumnCheckBox> getGroup() {
        Collections.sort(checkBoxes, new Comparator<ColumnCheckBox>() {
            @Override
            public int compare(ColumnCheckBox o1, ColumnCheckBox o2) {
                TranslateComparator translateComparator = new TranslateComparator();
                return translateComparator.compare(o1.getI18nName(), o2.getI18nName());
            }
        });
        ArrayList<ColumnCheckBox> boxes = new ArrayList<>();
        boxes.add(selectAll);
        boxes.addAll(checkBoxes);
        return boxes;
    }


    public void addCheckBoxGroupStateExecutor(CheckBoxSelectionPerformer executor){
        this.checkBoxPerformer = executor;
    }

    public void setEnable(boolean enable){
        selectAll.setEnabled(enable);
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
