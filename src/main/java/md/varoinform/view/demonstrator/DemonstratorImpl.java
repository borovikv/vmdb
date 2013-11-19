package md.varoinform.view.demonstrator;

import md.varoinform.model.entities.Enterprise;
import md.varoinform.util.ObservableEvent;
import md.varoinform.util.Observer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/30/13
 * Time: 10:33 AM
 */
public class DemonstratorImpl extends JPanel implements Demonstrator, Observer {

    private final Browser browser  = new Browser();
    private final TableView demonstrator = new TableView();
    private final JSplitPane splitPane;


    public DemonstratorImpl() {
        setLayout(new BorderLayout());

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(demonstrator), new JScrollPane(browser));
        splitPane.setResizeWeight(0.6);
        demonstrator.addListSelectionListener(new SelectionListener());

        add(splitPane);
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
        Enterprise enterprise = getSelectedEnterprise();
        demonstrator.updateUI();

        showEnterprise(enterprise);
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
        if ( event.getType() == ObservableEvent.STRUCTURE_CHANGED)
            demonstrator.fireViewStructureChanged();
    }

    private class SelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                Enterprise enterprise = demonstrator.getSelectedEnterprise();
                showEnterprise(enterprise);
            }
        }

    }

}
