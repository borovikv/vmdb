package md.varoinform.view.demonstrator;

import md.varoinform.controller.comparators.ColumnPriorityComparator;
import md.varoinform.model.dao.DAOTag;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.util.*;
import md.varoinform.util.Observable;
import md.varoinform.util.Observer;
import md.varoinform.view.OutputLabel;
import md.varoinform.view.dialogs.CheckBoxSelectionPerformer;
import md.varoinform.view.dialogs.TagDialog;
import md.varoinform.view.fieldgroup.ColumnCheckBox;
import md.varoinform.view.fieldgroup.FieldGroup;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/30/13
 * Time: 10:33 AM
 */
public class DemonstratorPanel extends JPanel implements Demonstrator, Observer, Observable {

    private final Browser browser  = new Browser();
    private final TableView demonstrator = new TableView();
    private Set<Observer> observers = new HashSet<>();
    private boolean painted = false;
    private final JSplitPane splitPane;
    private final JMenuItem addTagItem = new JMenuItem(ResourceBundleHelper.getString("tag", "add tag"));


    public DemonstratorPanel() {
        setLayout(new BorderLayout());

        addTagItem.setIcon(ImageHelper.getScaledImageIcon("/external-resources/icons/star.png", 16, 16));
        addTagItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tagTitle = TagDialog.getTag();
                DAOTag daoTag = new DAOTag();
                daoTag.createTag(tagTitle, demonstrator.getSelected());
                notifyObservers(new ObservableEvent(ObservableEvent.Type.TAGS_CHANGED));
            }
        });

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(demonstrator), new JScrollPane(browser));
        demonstrator.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Enterprise enterprise = demonstrator.getSelectedEnterprise();
                    showEnterprise(enterprise);
                }
            }
        });

        demonstrator.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    notifyObservers(new ObservableEvent(ObservableEvent.Type.DELETE));
                }
            }
        });

        demonstrator.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger() && e.getComponent() instanceof JTable) {
                    int column = demonstrator.columnAtPoint(e.getPoint());
                    int row = demonstrator.rowAtPoint(e.getPoint());
                    Object value = demonstrator.getModel().getValueAt(row, column);

                    JPopupMenu popup = createPopup(column, value);
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });


        demonstrator.getTableHeader().setComponentPopupMenu(createHeaderPopup());

        add(splitPane);
    }

    private JPopupMenu createHeaderPopup() {
        JPopupMenu popupMenu = new JPopupMenu();
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
        List<ColumnCheckBox> group = fieldGroup.getGroup();
        for (ColumnCheckBox columnCheckBox : group) {
            popupMenu.add(columnCheckBox);
        }
        return popupMenu;
    }


    private JPopupMenu createPopup(final int column, Object value) {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(addTagItem);
        popupMenu.addSeparator();

        Class<?> columnClass = demonstrator.getModel().getColumnClass(column);
        List<ActionListener> listeners = FilterListener.getListeners(columnClass, demonstrator, column, value);
        for (ActionListener listener : listeners) {
            String name = listener.toString();
            JMenuItem menuItem = new JMenuItem(ResourceBundleHelper.getString(name, name));
            menuItem.addActionListener(listener);
            popupMenu.add(menuItem);
        }

        return popupMenu;
    }



    @Override
    public void showResults(List<Enterprise> enterprises){
        demonstrator.showResults(enterprises);
        OutputLabel.instance.setResultCount(enterprises == null ? 0: enterprises.size());
    }

    @Override
    public List<Enterprise> getSelected(){
        return demonstrator.getSelected();
    }

    @Override
    public List<Enterprise> getALL() {
        return demonstrator.getALL();
    }

    @Override
    public void clear() {
        demonstrator.clear();
    }

    @Override
    public Enterprise getSelectedEnterprise() {
        return demonstrator.getSelectedEnterprise();
    }


    public void updateDisplay(){
        demonstrator.updateDisplay();
        showEnterprise(getSelectedEnterprise());
    }

    private void showEnterprise(Enterprise enterprise) {
        if ( enterprise != null ) {
            browser.setText(EnterpriseView.getView(enterprise));
        } else {
            browser.setText("");
        }
    }

    /*
        calls from settings dialog
     */
    @Override
    public void update(ObservableEvent event) {
        if ( event.getType() == ObservableEvent.Type.STRUCTURE_CHANGED) {
            demonstrator.fireViewStructureChanged();
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers(ObservableEvent event) {
        for (Observer observer : observers) {
            observer.update(event);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (!painted){
            painted = true;
            PreferencesHelper helper = new PreferencesHelper();
            double location = helper.getDivideLocation();
            splitPane.setDividerLocation(location);
            splitPane.setResizeWeight(location);
        }

    }

}