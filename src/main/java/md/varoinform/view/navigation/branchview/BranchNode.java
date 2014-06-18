package md.varoinform.view.navigation.branchview;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.entities.Language;
import md.varoinform.model.entities.Node;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/5/13
 * Time: 5:08 PM
 */
public class BranchNode extends DefaultMutableTreeNode implements Comparable {
    private Node node;

    public BranchNode(Object userObject) {
        super(userObject);
        this.node = (Node)userObject;
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
        if (node != null)
            return node.title(currentLanguage);
        return null;
    }

    public Node getNode() {
        return node;
    }
}