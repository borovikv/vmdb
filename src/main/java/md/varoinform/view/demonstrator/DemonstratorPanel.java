package md.varoinform.view.demonstrator;

import md.varoinform.controller.history.History;
import md.varoinform.controller.history.HistoryEvent;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.util.*;
import md.varoinform.util.observer.Observable;
import md.varoinform.util.observer.ObservableEvent;
import md.varoinform.util.observer.ObservableIml;
import md.varoinform.util.observer.Observer;
import md.varoinform.view.status.StatusBar;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
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
    private ObservableIml observable = new ObservableIml();
    private boolean painted = false;
    private final JSplitPane splitPane;


    public DemonstratorPanel() {
        setLayout(new BorderLayout());

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(demonstrator, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED),
                new JScrollPane(browser));

        demonstrator.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Enterprise enterprise = demonstrator.getSelectedEnterprise();
                    showEnterprise(enterprise);
                    StatusBar.instance.setRow(demonstrator.getSelectedRow() + 1);
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

        demonstrator.addMouseListener(new ContextMenuListener(demonstrator));
        demonstrator.getTableHeader().addMouseListener(new TableHeaderMouseAdapter(demonstrator));
        demonstrator.getColumnModel().addColumnModelListener(new MoveColumnListener(demonstrator));

        add(splitPane);
        History.instance.addObserver(this);
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

    @Override
    public void update(ObservableEvent event) {
        Object value = event.getValue();
        if ((event.getType() == ObservableEvent.Type.HISTORY_MOVE_BACK || event.getType() == ObservableEvent.Type.HISTORY_MOVE_FORWARD)
                && value instanceof HistoryEvent
                && ((HistoryEvent) value).getSource() instanceof FilterListener) {
                //noinspection unchecked
            RowSorter<EnterpriseTableModel> sorter = (RowSorter) ((HistoryEvent) value).getState();
            demonstrator.setModel(sorter.getModel());
            demonstrator.setRowSorter(sorter);
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observable.addObserver(observer);
    }

    @Override
    public void notifyObservers(ObservableEvent event) {
        observable.notifyObservers(event);
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