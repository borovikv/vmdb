package md.varoinform.view.demonstrator;

import md.varoinform.controller.comparators.ColumnPriorityComparator;
import md.varoinform.util.PreferencesHelper;
import md.varoinform.view.I18nCheckBox;
import md.varoinform.view.fieldgroup.CheckBoxSelectionPerformer;
import md.varoinform.view.fieldgroup.FieldGroup;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;

/**
* Created with IntelliJ IDEA.
* User: Vladimir Borovic
* Date: 6/17/14
* Time: 12:37 PM
*/
class TableHeaderMouseAdapter extends MouseAdapter {
    private final TableView demonstrator;

    TableHeaderMouseAdapter(TableView demonstrator) {
        this.demonstrator = demonstrator;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int column = demonstrator.getColumnModel().getColumnIndexAtX(e.getX());
        int columnCount = demonstrator.getColumnCount();
        if (column < 0) column = columnCount;
        column = Math.min(column, columnCount - 1);
        RowSorter rowSorter = (RowSorter) demonstrator.getRowSorter();
        if (rowSorter != null)
            rowSorter.setSortable(column, false);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        showPopupMenu(e);
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        showPopupMenu(e);
        demonstrator.columnOrderChanged();
    }

    private void showPopupMenu(MouseEvent e) {
        if (e.isPopupTrigger()) {
            JPopupMenu headerPopup = createHeaderPopup();
            headerPopup.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    private JPopupMenu createHeaderPopup() {
        final JPopupMenu popupMenu = new JPopupMenu();

        FieldGroup fieldGroup = new FieldGroup();
        fieldGroup.addCheckBoxGroupStateExecutor(new CheckBoxSelectionPerformer() {
            @Override
            public void perform(List<String> names) {
                Collections.sort(names, new ColumnPriorityComparator());
                PreferencesHelper preferencesHelper = new PreferencesHelper();
                preferencesHelper.putUserFields(names);
                demonstrator.fireViewStructureChanged();
            }
        });
        List<I18nCheckBox> group = fieldGroup.getGroup();
        for (I18nCheckBox columnCheckBox : group) {
            popupMenu.add(columnCheckBox);
        }

        return popupMenu;
    }
}
