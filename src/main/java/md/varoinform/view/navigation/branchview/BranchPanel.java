package md.varoinform.view.navigation.branchview;

import md.varoinform.util.Observable;
import md.varoinform.util.ObservableEvent;
import md.varoinform.util.Observer;
import md.varoinform.view.navigation.FilteringDocumentListener;
import md.varoinform.view.navigation.tags.AutoCompleteTextField;

import javax.swing.*;
import java.awt.BorderLayout;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/3/13
 * Time: 2:58 PM
 */
public class BranchPanel extends JPanel implements Observable {
    private final JTextField textField = new AutoCompleteTextField();
    private final BranchTree branchTree = new BranchTree();
    public BranchPanel() {
        textField.getDocument().addDocumentListener(new FilteringDocumentListener(branchTree));
        setLayout(new BorderLayout());
        add(textField, BorderLayout.NORTH);
        add(new JScrollPane(branchTree));
    }

    @Override
    public void addObserver(Observer observer) {
        branchTree.addObserver(observer);
    }

    @Override
    public void notifyObservers(ObservableEvent event) {
    }

    public void updateDisplay() {
        branchTree.updateRoot();
    }

    public void select(Object obj) {
        branchTree.select(obj);
    }

    public void clearSelection() {
        branchTree.clearSelection();
    }

    public List<Long> getNodes(){
        return branchTree.getAllChildrenId();
    }
}
