package md.varoinform.view.fieldgroup;

import md.varoinform.controller.sorter.TranslateComparator;
import md.varoinform.controller.entityproxy.EnterpriseProxy;
import md.varoinform.util.PreferencesHelper;
import md.varoinform.view.I18nCheckBox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 5/14/14
 * Time: 10:31 AM
 */
public class FieldGroup {
    private final Set<String> selectedFields = new TreeSet<>();
    private final List<I18nCheckBox> checkBoxes = new ArrayList<>();
    private CheckBoxSelectionPerformer checkBoxPerformer;
    private I18nCheckBox selectAll = new I18nCheckBox("select_all");

    public FieldGroup() {
        List<String> userFields = new PreferencesHelper().getUserFields();
        for (String field : EnterpriseProxy.getFields()) {
            boolean selected = userFields.contains(field);

            I18nCheckBox checkBox = createCheckBox(field, selected);
            checkBoxes.add(checkBox);

            if (selected) {
                selectedFields.add(field);
            }
        }

        if (selectedFields.size() == checkBoxes.size()){
            selectAll.setSelected(true);
        }

        selectAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (I18nCheckBox checkBox : checkBoxes) {
                    checkBox.setSelected(selectAll.isSelected());
                    performSelect(checkBox);
                }
            }
        });
    }

    private I18nCheckBox createCheckBox(String field, final boolean selected) {
        I18nCheckBox checkBox = new I18nCheckBox(field);
        checkBox.setSelected(selected);
        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectAll.setSelected(false);
                performSelect((I18nCheckBox) e.getSource());
            }
        });
        return checkBox;
    }

    public void performSelect(I18nCheckBox checkBox) {
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

    public List<I18nCheckBox> getGroup() {
        Collections.sort(checkBoxes, new Comparator<I18nCheckBox>() {
            @Override
            public int compare(I18nCheckBox o1, I18nCheckBox o2) {
                TranslateComparator translateComparator = new TranslateComparator();
                return translateComparator.compare(o1.getI18nName(), o2.getI18nName());
            }
        });
        ArrayList<I18nCheckBox> boxes = new ArrayList<>();
        boxes.add(selectAll);
        boxes.addAll(checkBoxes);
        return boxes;
    }


    public void addCheckBoxGroupStateExecutor(CheckBoxSelectionPerformer executor){
        this.checkBoxPerformer = executor;
    }

    public void setEnable(boolean enable){
        selectAll.setEnabled(enable);
        for (I18nCheckBox checkBox : checkBoxes) {
            checkBox.setEnabled(enable);
        }
    }

    public void updateDisplay(){
        for (I18nCheckBox checkBox : checkBoxes) {
            checkBox.updateDisplay();
        }
    }

}
