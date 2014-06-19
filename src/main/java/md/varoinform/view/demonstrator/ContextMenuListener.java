package md.varoinform.view.demonstrator;

import md.varoinform.util.ResourceBundleHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
* Created with IntelliJ IDEA.
* User: Vladimir Borovic
* Date: 6/17/14
* Time: 12:42 PM
*/
class ContextMenuListener extends MouseAdapter {

    private final TableView demonstrator;

    ContextMenuListener(TableView demonstrator) {
        this.demonstrator = demonstrator;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        showPopupMenu(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        showPopupMenu(e);
    }

    public void showPopupMenu(MouseEvent e) {
        if (e.isPopupTrigger() && e.getComponent() instanceof JTable) {
            int column = demonstrator.columnAtPoint(e.getPoint());
            int row = demonstrator.rowAtPoint(e.getPoint());
            Object value = null;
            try {
                value = demonstrator.getValueAt(row, column);
            } catch (Exception ignored){}
            JPopupMenu popup = createPopup(column, value);
            popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    private JPopupMenu createPopup(final int column, Object value) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem removeFilter = new JMenuItem(ResourceBundleHelper.getString("remove_filter", "Remove filter"));
        removeFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FilterListener.remove(demonstrator, column);
            }
        });
        popupMenu.add(removeFilter);
        popupMenu.addSeparator();

        Class<?> columnClass = demonstrator.getModel().getColumnClass(column);
        List<ActionListener> listeners = FilterListener.getListeners(columnClass, demonstrator, column, value);
        for (ActionListener listener : listeners) {
            String name = listener.toString();
            JMenuItem menuItem = new JMenuItem(ResourceBundleHelper.getString(name, name));
            menuItem.addActionListener(listener);

            popupMenu.add(menuItem);
        }
        popupMenu.addSeparator();


        JMenuItem sortColumnItem = new JMenuItem(getText(column, "ascending"));
        sortColumnItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                demonstrator.sort(column, SortOrder.ASCENDING);
            }
        });
        popupMenu.add(sortColumnItem);

        JMenuItem sortColumnDescItem = new JMenuItem(getText(column, "descending"));
        sortColumnDescItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                demonstrator.sort(column, SortOrder.DESCENDING);
            }
        });
        popupMenu.add(sortColumnDescItem);

        return popupMenu;
    }

    private String getText(int column, String type) {
        String name = demonstrator.getModel().getColumnName(column);
        name = ResourceBundleHelper.getString(name, name);
        type = ResourceBundleHelper.getString(type, type);
        String format = ResourceBundleHelper.getString("filter_text_format", "Sort column [%s] %s");
        return String.format(format, name, type);
    }

}
