package md.varoinform.view.navigation.branchview;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.entities.Node;
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
public class BranchNode extends DefaultMutableTreeNode implements Comparable {
    private Node treeNode;
    private List<Long> allChildrenId;

    public BranchNode(Object userObject) {
        super(userObject);
        this.treeNode = (Node)userObject;
    }

    @Override
    public void insert(final MutableTreeNode newChild, final int childIndex) {
        super.insert(newChild, childIndex);
        Collections.sort(this.children);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public int compareTo(final Object o) {
        if (o instanceof BranchNode)
            return getTitle().compareToIgnoreCase(((BranchNode) o).getTitle());
        return -2;
    }

    public String getTitle(){
        Language currentLanguage = LanguageProxy.instance.getCurrentLanguage();
        if (treeNode != null)
            return treeNode.title(currentLanguage);
        return null;
    }

    public Node getTreeNode() {
        return treeNode;
    }

    public List<Long> getAllChildrenId(){
        if (allChildrenId == null){
            allChildrenId = new ArrayList<>();
            fillAllChildrenId(treeNode, allChildrenId);
        }
        return allChildrenId;
    }

    private void fillAllChildrenId(Node treeNode, List<Long> result) {
        if (treeNode == null) return;

        result.add(treeNode.getId());

        for (Node child : treeNode.getChildren()) {
            fillAllChildrenId(child, result);
        }
    }
}