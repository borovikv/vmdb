package md.varoinform.view.navigation.branchview;

import md.varoinform.model.dao.NodeDao;
import md.varoinform.model.entities.TreeNode;
import md.varoinform.util.*;
import md.varoinform.view.navigation.FilteringNavigator;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/12/13
 * Time: 11:22 AM
 */
public class BranchTree extends JTree implements Observable, FilteringNavigator {
    private BranchNode root = new BranchNode(null);
    private boolean needToProcess = true;
    private List<Observer> observers = new ArrayList<>();
    private boolean programatically = false;
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
                    tree.select(path, false);
                }
            }
        });
    }

    public static boolean isTreePath(Object value) {
        return value instanceof TreePath;
    }


    public void updateRoot(){
        needToProcess = false;
        TreeNode treeNode = getBranchFromSelected();
        filter(text);
        scrollToBranch(treeNode);
        updateUI();
        needToProcess = true;
    }

    private TreeNode getBranchFromSelected() {
        BranchNode branchNode = ((BranchNode)getLastSelectedPathComponent());
        if(branchNode != null)
            return branchNode.getTreeNode();
        return null;
    }

    private void createTree(List<TreeNode> nodes, BranchNode root) {
        if (nodes.isEmpty()) return;
        for (TreeNode treeNode : nodes) {
            BranchNode branchNode = new BranchNode(treeNode);
            root.add(branchNode);
            createTree(treeNode.getChildren(), branchNode);
        }
    }

    private void scrollToBranch(TreeNode treeNode) {
        if (treeNode != null){
            TreePath treePath = getTreePathForBranch(treeNode);

            scrollPathToVisible(treePath);
            select(treePath, true);
        }
    }

    private TreePath getTreePathForBranch(TreeNode treeNode) {
        DefaultTreeModel defaultTreeModel = ( DefaultTreeModel ) treeModel;
        BranchNode node = findNode(treeNode, root );
        javax.swing.tree.TreeNode[] nodes = defaultTreeModel.getPathToRoot( node );
        return new TreePath( nodes );
    }


    private BranchNode findNode(TreeNode treeNode, BranchNode root) {
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


    public void select(Object object, boolean programatically){
        if (object instanceof TreePath){
            this.programatically = programatically;
            setSelectionPath((TreePath) object);
            performSelect();
            this.programatically = false;
        }
    }

    private void performSelect() {
        if (!needToProcess) return;

        if (!programatically) {
            notifyObservers(new ObservableEvent(ObservableEvent.BRANCH_SELECTED_BY_USER, getSelectionPath()));
        }
        notifyObservers(new ObservableEvent(ObservableEvent.BRANCH_SELECTED));
    }

    public List<Long> getAllChildrenId() {
        BranchNode node = (BranchNode) getLastSelectedPathComponent();
        if (node == null) return null;

        return node.getAllChildrenId();
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
    public void filter(String text) {
        root.removeAllChildren();
        NodeDao nodeDao = new NodeDao();
        List<TreeNode> topNodes =  nodeDao.startWith(text.trim());
        createTree(topNodes, root);

        DefaultTreeModel defaultTreeModel = (DefaultTreeModel) treeModel;
        defaultTreeModel.setRoot(root);
        this.text = text;
    }
}
