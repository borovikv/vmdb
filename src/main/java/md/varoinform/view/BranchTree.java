package md.varoinform.view;

import md.varoinform.model.dao.BranchDao;
import md.varoinform.model.entities.Branch;
import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/12/13
 * Time: 11:22 AM
 */
public class BranchTree extends JTree {
    public BranchTree() {
        BranchTreeNode root = createRoot();
        setModel(new DefaultTreeModel(root));
        setCellRenderer(new BranchCellRenderer());
        setRootVisible(false);
        setShowsRootHandles(true);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    private BranchTreeNode createRoot() {
        Branch branchRoot = BranchDao.getRoot();
        BranchTreeNode root = new BranchTreeNode(branchRoot);
        createTree(branchRoot, root);
        return root;
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
}
