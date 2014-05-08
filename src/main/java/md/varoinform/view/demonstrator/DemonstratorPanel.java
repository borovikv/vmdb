package md.varoinform.view.demonstrator;

import md.varoinform.model.dao.DAOTag;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.util.Observable;
import md.varoinform.util.ObservableEvent;
import md.varoinform.util.Observer;
import md.varoinform.util.PreferencesHelper;
import md.varoinform.view.dialogs.TagDialog;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
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
    private Map<Integer, RowFilter<Object, Object>> filters = new HashMap<>();
    private Map<Integer, String> filtersText = new HashMap<>();
    private TableRowSorter<TableModel> sorter ;


    public DemonstratorPanel() {
        setLayout(new BorderLayout());

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(demonstrator), new JScrollPane(browser));
        demonstrator.setRowSorter(sorter);
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
                    notifyObservers(new ObservableEvent(ObservableEvent.DELETE));
                }
            }
        });

        demonstrator.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger() && e.getComponent() instanceof JTable ) {
                    int i = demonstrator.columnAtPoint(e.getPoint());

                    JPopupMenu popup = createPopup(i);
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        add(splitPane);
    }


    private JPopupMenu createPopup(final int column) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem addTagItem = new JMenuItem("add tag");
        addTagItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tagTitle = TagDialog.getTag();
                DAOTag daoTag = new DAOTag();
                daoTag.createTag(tagTitle, demonstrator.getSelected());
                notifyObservers(new ObservableEvent(ObservableEvent.TAGS_CHANGED));
            }
        });
        popupMenu.add(addTagItem);
        popupMenu.addSeparator();

        JMenuItem filter = new JMenuItem("filter");
        filter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String result = JOptionPane.showInputDialog("filter by", filtersText.get(column));
                newFilter(result, ".*" +result  + ".*", column);
            }
        });
        popupMenu.add(filter);

        JMenuItem notContainFilter = new JMenuItem("not contain");
        notContainFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String result = JOptionPane.showInputDialog("filter by", filtersText.get(column));

                newFilter(result, "^((?!" + result + ").)*$", column);
            }
        });
        popupMenu.add(notContainFilter);


        return popupMenu;
    }

    private void newFilter(String text, String pattern, int column) {

        RowFilter<TableModel, Object> rf;
        //If current expression doesn't parse, don't update.
        try {
            filters.put(column, RowFilter.regexFilter(pattern , column));
            filtersText.put(column, text);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        rf = RowFilter.andFilter(filters.values());
        sorter = new TableRowSorter<>(demonstrator.getModel());
        sorter.setRowFilter(rf);
        demonstrator.setRowSorter(sorter);
    }

    @Override
    public void showResults(List<Enterprise> enterprises){
        demonstrator.showResults(enterprises);
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
        if ( event.getType() == ObservableEvent.STRUCTURE_CHANGED) {
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