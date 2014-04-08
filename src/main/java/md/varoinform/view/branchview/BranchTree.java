package md.varoinform.view.branchview;

import md.varoinform.model.dao.BranchDao;
import md.varoinform.model.dao.EnterpriseDao;
import md.varoinform.model.entities.TreeNode;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.util.*;
import md.varoinform.view.NavigationPaneList;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
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
public class BranchTree extends JTree implements Observable, NavigationPaneList {
    private BranchTreeNode root;
    private boolean needToProcess = true;
    private List<Observer> observers = new ArrayList<>();
    private boolean programatically = false;

    public BranchTree() {
        root = createRoot();
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
                if (path != null){
                    tree.clearSelection();
                    tree.select(path);
                }
            }
        });

        addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                BranchTree tree = (BranchTree) e.getSource();
                if (!needToProcess) return;

                BranchTreeNode node = (BranchTreeNode) tree.getLastSelectedPathComponent();
                if (node == null) return;

                java.util.List<Long> allChildren = node.getAllChildrenId();
                java.util.List<Enterprise> enterprises = EnterpriseDao.getEnterprisesByBranchId(allChildren);

                if (!programatically) {
                    notifyObservers(new ObservableEvent(ObservableEvent.BRANCH_SELECTED_BY_USER, tree.getSelectionPath()));
                }
                notifyObservers(new ObservableEvent(ObservableEvent.BRANCH_SELECTED, enterprises));

            }
        });
    }

    private BranchTreeNode createRoot() {
        TreeNode treeNodeRoot = BranchDao.getRoot();
        if (treeNodeRoot == null) return null;
        return new BranchTreeNode(treeNodeRoot);
    }

    private void createTree(TreeNode treeNodeRoot, BranchTreeNode root) {
        if (treeNodeRoot == null) return;
        /*for (TreeNode treeNode : treeNodeRoot.getChildren()) {
            BranchTreeNode branchTreeNode = new BranchTreeNode(treeNode);
            root.add(branchTreeNode);
            createTree(treeNode, branchTreeNode);
        }
        */
    }


    public static boolean isTreePath(Object value) {
        return value instanceof TreePath;
    }

    public void updateRoot(){
        if (root == null) return;

        needToProcess = false;
        TreeNode treeNode = getBranchFromSelected();

        root.removeAllChildren();
        createTree(root.getTreeNode(), root);
        DefaultTreeModel defaultTreeModel = (DefaultTreeModel) treeModel;
        defaultTreeModel.setRoot(root);

        scrollToBranch(treeNode);
        updateUI();
        needToProcess = true;
    }

    private TreeNode getBranchFromSelected() {
        BranchTreeNode branchTreeNode = ((BranchTreeNode)getLastSelectedPathComponent());
        if(branchTreeNode != null)
            return branchTreeNode.getTreeNode();
        return null;
    }

    private void scrollToBranch(TreeNode treeNode) {
        if (treeNode != null){
            TreePath treePath = getTreePathForBranch(treeNode);

            scrollPathToVisible(treePath);
            select(treePath);
        }
    }

    private TreePath getTreePathForBranch(TreeNode treeNode) {
        DefaultTreeModel defaultTreeModel = ( DefaultTreeModel ) treeModel;
        BranchTreeNode node = findNode(treeNode, root );
        javax.swing.tree.TreeNode[] nodes = defaultTreeModel.getPathToRoot( node );
        return new TreePath( nodes );
    }


    private BranchTreeNode findNode(TreeNode treeNode, BranchTreeNode root) {
        if (treeNode.equals(root.getTreeNode())){
            return root;
        }
        BranchTreeNode branchTreeNode;
        for (int i = 0; i < root.getChildCount(); i++) {
            branchTreeNode = findNode(treeNode, (BranchTreeNode)root.getChildAt(i));
            if (branchTreeNode != null)
                return branchTreeNode;
        }
        return null;
    }


    @Override
    public void updateSelection() {
        TreePath path = getSelectionPath();
        clearSelection();
        select(path);
    }

    private void setSelectionPath(TreePath path, boolean programatically) {
        this.programatically = programatically;
        setSelectionPath(path);
    }

    public void select(Object object){
        if (object instanceof TreePath){
            setSelectionPath((TreePath) object, true);
            this.programatically = false;
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
}
