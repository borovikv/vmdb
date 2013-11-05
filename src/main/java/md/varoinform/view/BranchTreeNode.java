package md.varoinform.view;

import md.varoinform.model.entities.Branch;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/5/13
 * Time: 5:08 PM
 */
public class BranchTreeNode  extends DefaultMutableTreeNode implements Comparable {
    /*public BranchTreeNode(String name) {
        super(name);
    }
    */
    Branch branch;
    public BranchTreeNode(Object userObject) {
        super(userObject);
        this.branch = (Branch)userObject;
    }

    @Override
    public void insert(final MutableTreeNode newChild, final int childIndex) {
        super.insert(newChild, childIndex);
        Collections.sort(this.children);
    }
    public int compareTo(final Object o) {
        if (o instanceof BranchTreeNode)
            return getTitle().compareToIgnoreCase(((BranchTreeNode)o).getTitle());
        return -2;
    }

    public String getTitle(){
        return this.branch.getTitles().get(0).getTitle();
    }

    public Branch getBranch() {
        return branch;
    }
}