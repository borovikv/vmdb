package md.varoinform.view.navigation.branchview;

import md.varoinform.controller.history.History;
import md.varoinform.controller.history.HistoryEvent;
import md.varoinform.model.dao.NodeDao;
import md.varoinform.model.entities.Node;
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
        setRootVisible(false);
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
        Node treeNode = getBranchFromSelected();
        filter(text);
        scrollToBranch(treeNode);
        updateUI();
        needToProcess = true;
    }

    private Node getBranchFromSelected() {
        BranchNode branchNode = ((BranchNode)getLastSelectedPathComponent());
        if(branchNode != null)
            return branchNode.getTreeNode();
        return null;
    }

    private void createTree(List<Node> nodes, BranchNode root) {
        if (nodes.isEmpty()) return;
        for (Node treeNode : nodes) {
            BranchNode branchNode = new BranchNode(treeNode);
            root.add(branchNode);
            createTree(treeNode.getChildren(), branchNode);
        }
    }

    private void scrollToBranch(Node treeNode) {
        if (treeNode != null){
            TreePath treePath = getTreePathForBranch(treeNode);

            scrollPathToVisible(treePath);
            select(treePath);
        }
    }

    private TreePath getTreePathForBranch(Node treeNode) {
        DefaultTreeModel defaultTreeModel = ( DefaultTreeModel ) treeModel;
        BranchNode node = findNode(treeNode, root );
        javax.swing.tree.TreeNode[] nodes = defaultTreeModel.getPathToRoot( node );
        return new TreePath( nodes );
    }


    private BranchNode findNode(Node treeNode, BranchNode root) {
        if (treeNode.equals(root.getTreeNode())){
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
        if (object instanceof TreePath){
            setSelectionPath((TreePath) object);
            performSelect();
        }
    }

    private void performSelect() {
        if (!needToProcess) return;

        notifyObservers(new ObservableEvent(ObservableEvent.Type.BRANCH_SELECTED));
        History.instance.add(new HistoryEvent(this, getSelectionPath()));
    }

    public List<Long> getAllChildrenId() {
        BranchNode node = (BranchNode) getLastSelectedPathComponent();
        if (node == null) return null;

        return node.getAllChildrenId();
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
        NodeDao nodeDao = new NodeDao();
        List<Node> topNodes =  nodeDao.startWith(text.trim());
        createTree(topNodes, root);

        DefaultTreeModel defaultTreeModel = (DefaultTreeModel) treeModel;
        defaultTreeModel.setRoot(root);
        this.text = text;
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
}
