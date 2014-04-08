package md.varoinform.model.entities;

import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;


/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 4:17 PM
 */
@Entity
@Table(name = "EXPORTED_DB.DB_good")
public class Good extends TitleContainer<GoodTitle> {
    @Column(name = "branch_id")
    public Long getBranchID() {
        return branchID;
    }

    public void setBranchID(Long branchID) {
        this.branchID = branchID;
    }

    //private TreeNode treeNode;
    private Long branchID;

    public Good() {
    }

    /*
    public Good(TreeNode treeNode) {
        setTreeNode(treeNode);
    }
    @ManyToOne
    @JoinColumn(name = "branch_id")
    @IndexedEmbedded
    public TreeNode getTreeNode() {
        return treeNode;
    }

    public void setTreeNode(TreeNode treeNode) {
        this.treeNode = treeNode;
    }
    */
    @Override
    public String toString() {
        return "id = " + getId() + " titles = " + getTitles() + " branch_id = " + branchID;
    }
}
