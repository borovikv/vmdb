package md.varoinform.view.navigation.branchview;

import md.varoinform.util.observer.Observable;
import md.varoinform.util.observer.ObservableEvent;
import md.varoinform.util.observer.Observer;
import md.varoinform.view.navigation.AutoCompleteTextField;
import md.varoinform.view.navigation.FilteringDocumentListener;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/3/13
 * Time: 2:58 PM
 */
public class BranchPanel extends JPanel implements Observable {
    private final AutoCompleteTextField textField = new AutoCompleteTextField("filter_tree");
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
        textField.updateDisplay();
    }


    public void selectRoot(){
        branchTree.selectRoot();

    }

    public void clearSelection() {
        branchTree.clearSelection();
    }

    public Long getNode() {
        return ((BranchNode)branchTree.getLastSelectedPathComponent()).getNode();
    }
}
