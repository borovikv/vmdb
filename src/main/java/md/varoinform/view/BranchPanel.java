package md.varoinform.view;

import md.varoinform.util.Observable;
import md.varoinform.util.ObservableEvent;
import md.varoinform.util.Observer;
import md.varoinform.view.branchview.BranchTree;

import javax.swing.*;
import java.awt.BorderLayout;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/3/13
 * Time: 2:58 PM
 */
public class BranchPanel extends JPanel implements NavigationPaneList, Observable {
    private final BranchTree branchTree = new BranchTree();
    public BranchPanel() {
        setLayout(new BorderLayout());
        add(new JScrollPane(branchTree));
    }

    @Override
    public void updateSelection() {
        branchTree.updateSelection();
    }

    @Override
    public void addObserver(Observer observer) {
        branchTree.addObserver(observer);
    }

    @Override
    public void notifyObservers(ObservableEvent event) {
    }

    public void updateRoot() {
        branchTree.updateRoot();
    }

    public void select(Object obj) {
        branchTree.select(obj, true);
    }

    public void clearSelection() {
        branchTree.clearSelection();
    }

    public Object getSelectionPath() {
        return branchTree.getSelectionPath();
    }

    public List<Long> getNodes(){
        return branchTree.getAllChildrenId();
    }
}
