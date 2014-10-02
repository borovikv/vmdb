package md.varoinform.view.navigation.branchview;

import md.varoinform.controller.cache.BranchCache;

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
    private final Long id;
    private final String node;
    private final static long ROOT_ID = 1L;

    public BranchNode(Long id) {
        super(id);
        if (id != null) {
            this.id = id;
            this.node = BranchCache.instance.getTitle(id);
        } else {
            this.id = ROOT_ID;
            node = "root";
        }
    }

    public boolean isRoot(){
        return id.equals(ROOT_ID);
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
        return node;
    }

    public Long getNode() {
        return id;
    }
}