package md.varoinform.view.branchview;

import md.varoinform.model.dao.BranchDao;
import md.varoinform.model.entities.Branch;
import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/12/13
 * Time: 11:22 AM
 */
public class BranchTree extends JTree {


    private BranchTreeNode root;
    private boolean needToProcess = true;

    public BranchTree() {
        root = createRoot();
        setCellRenderer(new BranchCellRenderer());
        setRootVisible(false);
        setShowsRootHandles(true);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTree tree = (JTree) e.getSource();
                Point point = e.getPoint();
                TreePath path = tree.getPathForLocation(point.x, point.y);
                if (path != null){
                    tree.clearSelection();
                    tree.setSelectionPath(path);
                }
            }
        });
    }

    private BranchTreeNode createRoot() {
        Branch branchRoot = BranchDao.getRoot();
        return new BranchTreeNode(branchRoot);
    }

    private void createTree(Branch branchRoot, BranchTreeNode root) {
        for (Branch branch : branchRoot.getChildren()) {
            BranchTreeNode branchTreeNode = new BranchTreeNode(branch);
            root.add(branchTreeNode);
            createTree(branch, branchTreeNode);
        }
    }


    public static boolean isTreePath(Object value) {
        return value instanceof TreePath;
    }

    public void updateRoot(){
        needToProcess = false;
        Branch branch = getBranchFromSelected();

        root.removeAllChildren();
        createTree(root.getBranch(), root);
        DefaultTreeModel defaultTreeModel = (DefaultTreeModel) treeModel;
        defaultTreeModel.setRoot(root);

        scrollToBranch(branch);
        updateUI();
        needToProcess = true;
    }

    private Branch getBranchFromSelected() {
        BranchTreeNode branchTreeNode = ((BranchTreeNode)getLastSelectedPathComponent());
        if(branchTreeNode != null)
            return branchTreeNode.getBranch();
        return null;
    }

    private void scrollToBranch(Branch branch) {
        if (branch != null){
            TreePath treePath = getTreePathForBranch(branch);

            scrollPathToVisible(treePath);
            setSelectionPath(treePath);
        }
    }

    private TreePath getTreePathForBranch(Branch branch) {
        DefaultTreeModel defaultTreeModel = ( DefaultTreeModel ) treeModel;
        BranchTreeNode node = findNode( branch, root );
        TreeNode[] nodes = defaultTreeModel.getPathToRoot( node );
        return new TreePath( nodes );
    }


    private BranchTreeNode findNode(Branch branch, BranchTreeNode root) {
        if (branch.getId().equals(root.getBranch().getId())){
            return root;
        }
        BranchTreeNode branchTreeNode = null;
        for (int i = 0; i < root.getChildCount(); i++) {
            branchTreeNode = findNode(branch, (BranchTreeNode)root.getChildAt(i));
            if (branchTreeNode != null)
                return branchTreeNode;
        }
        return branchTreeNode;
    }

    public boolean isNeedToProcess(){
        return needToProcess;
    }

    public void updateSelection() {
        TreePath path = getSelectionPath();
        clearSelection();
        setSelectionPath(path);
    }
}
