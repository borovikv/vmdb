package md.varoinform.view;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.entities.Branch;

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
    private Branch branch;
    private List<Long> allChildren;

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
        return this.branch.title(LanguageProxy.getInstance().getCurrentLanguage());
    }

    public Branch getBranch() {
        return branch;
    }
    public List<Long> getAllChildren(){
        if (allChildren == null){
            allChildren = getAllChildren(branch, null);
        }
        return allChildren;
    }

    private List<Long> getAllChildren(Branch branch, List<Long> result) {
        if ( result == null ) {
            result = new ArrayList<>();
        }
        result.add(branch.getId());
        for (Branch child : branch.getChildren()) {
            getAllChildren(child, result);
        }
        return result;
    }
}