package md.varoinform.view.branchview;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.entities.Branch;
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
    private Branch branch;
    private List<Long> allChildrenId;

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
        Language currentLanguage = LanguageProxy.instance.getCurrentLanguage();
        if (branch != null)
            return branch.title(currentLanguage);
        return null;
    }

    public Branch getBranch() {
        return branch;
    }

    public List<Long> getAllChildrenId(){
        if (allChildrenId == null){
            allChildrenId = new ArrayList<>();
            fillAllChildrenId(branch, allChildrenId);
        }
        return allChildrenId;
    }

    private void fillAllChildrenId(Branch branch, List<Long> result) {
        result.add(branch.getId());

        for (Branch child : branch.getChildren()) {
            fillAllChildrenId(child, result);
        }
    }
}