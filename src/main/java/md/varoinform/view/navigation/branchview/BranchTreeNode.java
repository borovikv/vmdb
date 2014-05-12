package md.varoinform.view.navigation.branchview;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.entities.TreeNode;
import md.varoinform.model.entities.Language;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/5/13
 * Time: 5:08 PM
 */
public class BranchTreeNode  extends DefaultMutableTreeNode implements Comparable {
    private TreeNode treeNode;
    private List<Long> allChildrenId;

    public BranchTreeNode(Object userObject) {
        super(userObject);
        this.treeNode = (TreeNode)userObject;
    }

    @Override
    public void insert(final MutableTreeNode newChild, final int childIndex) {
        super.insert(newChild, childIndex);
        Collections.sort(this.children);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public int compareTo(final Object o) {
        if (o instanceof BranchTreeNode)
            return getTitle().compareToIgnoreCase(((BranchTreeNode)o).getTitle());
        return -2;
    }

    public String getTitle(){
        Language currentLanguage = LanguageProxy.instance.getCurrentLanguage();
        if (treeNode != null)
            return treeNode.title(currentLanguage);
        return null;
    }

    public TreeNode getTreeNode() {
        return treeNode;
    }

    public List<Long> getAllChildrenId(){
        if (allChildrenId == null){
            allChildrenId = new ArrayList<>();
            fillAllChildrenId(treeNode, allChildrenId);
        }
        return allChildrenId;
    }

    private void fillAllChildrenId(TreeNode treeNode, List<Long> result) {
        result.add(treeNode.getId());

        for (TreeNode child : treeNode.getChildren()) {
            fillAllChildrenId(child, result);
        }
    }
}