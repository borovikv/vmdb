package md.varoinform.view.navigation.branchview;

import md.varoinform.controller.cache.BranchCache;
import md.varoinform.controller.history.History;
import md.varoinform.controller.history.HistoryEvent;
import md.varoinform.util.observer.Observable;
import md.varoinform.util.observer.ObservableEvent;
import md.varoinform.util.observer.ObservableIml;
import md.varoinform.util.observer.Observer;
import md.varoinform.view.navigation.FilteringNavigator;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/12/13
 * Time: 11:22 AM
 */
public class BranchTree extends JTree implements Observable, Observer, FilteringNavigator{
    private BranchNode root = new BranchNode(null);
    private boolean needToProcess = true;
    private ObservableIml observable = new ObservableIml();
    private String text = "";

    public BranchTree() {
        setCellRenderer(new BranchCellRenderer());
        setRootVisible(true);
        setShowsRootHandles(true);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                BranchTree tree = (BranchTree) e.getSource();
                Point point = e.getPoint();
                TreePath path = tree.getPathForLocation(point.x, point.y);
                if (path != null) {
                    tree.clearSelection();
                    tree.select(path);
                }
            }
        });
        History.instance.addObserver(this);
    }

    public void updateRoot(){
        needToProcess = false;
        Long treeNodeId = getBranchFromSelected();
        filter(text);
        select(treeNodeId);
        updateUI();
        needToProcess = true;
    }

    private Long getBranchFromSelected() {
        BranchNode branchNode = ((BranchNode)getLastSelectedPathComponent());
        if(branchNode != null)
            return branchNode.getNode();
        return null;
    }


    private TreePath getTreePathForBranch(Long treeNode) {
        DefaultTreeModel defaultTreeModel = ( DefaultTreeModel ) treeModel;
        BranchNode node = findNode(treeNode, root );
        javax.swing.tree.TreeNode[] nodes = defaultTreeModel.getPathToRoot( node );
        return new TreePath( nodes );
    }


    private BranchNode findNode(Long treeNode, BranchNode root) {
        if (treeNode.equals(root.getNode())){
            return root;
        }
        BranchNode branchNode;
        for (int i = 0; i < root.getChildCount(); i++) {
            branchNode = findNode(treeNode, (BranchNode)root.getChildAt(i));
            if (branchNode != null)
                return branchNode;
        }
        return null;
    }


    public void select(Object object){
        if (object == null) return;

        TreePath path = null;
        if (object instanceof BranchNode){
            path = getTreePathForBranch(((BranchNode) object).getNode());
        } else if (object instanceof Number){
            path = getTreePathForBranch((Long) object);
        }
        else if (object instanceof TreePath){
            path = (TreePath) object;
        }
        if (path != null) {
            setSelectionPath(path);
            scrollPathToVisible(path);
            performSelect();
        }
    }

    private void performSelect() {
        if (!needToProcess) return;

        notifyObservers(new ObservableEvent(ObservableEvent.Type.BRANCH_SELECTED));
        History.instance.add(new HistoryEvent(this, getSelectionPath()));
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
    public void filter(String text) {
        root.removeAllChildren();
        List<Long> ids = BranchCache.instance.startWith(text);
        createTree(ids, root);

        DefaultTreeModel defaultTreeModel = (DefaultTreeModel) treeModel;
        defaultTreeModel.setRoot(root);
        this.text = text;
    }

    private void createTree(List<Long> ids, BranchNode root) {
        if (ids.isEmpty()) return;
        for (Long id : ids) {
            BranchNode branchNode = new BranchNode(id);
            root.add(branchNode);
            createTree(BranchCache.instance.getChildren(id), branchNode);
        }
    }

    @Override
    public void update(ObservableEvent event) {
        ObservableEvent.Type type = event.getType();
        Object value = event.getValue();
        if ((type == ObservableEvent.Type.HISTORY_MOVE_FORWARD || type == ObservableEvent.Type.HISTORY_MOVE_BACK) && value instanceof HistoryEvent){
            if (((HistoryEvent) value).getSource() == this){
                select(((HistoryEvent) value).getState());
            } else {
                clearSelection();
            }
        }
    }

    public void selectRoot() {
        select(root);
    }
}
