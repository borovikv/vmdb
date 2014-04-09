package md.varoinform.model.entities;

import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/4/14
 * Time: 9:51 AM
 */
@Entity
@Table(name = "EXPORTED_DB.DB_good2")
public class Good2 extends TitleContainer<Good2Title> {
    private Set<TreeNode> treeNodes = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "EXPORTED_DB.DB_good_tree", joinColumns = @JoinColumn(name = "good_id"), inverseJoinColumns = @JoinColumn(name = "node_id"))
    @IndexedEmbedded
    public Set<TreeNode> getTreeNodes() {
        return treeNodes;
    }

    public void setTreeNodes(Set<TreeNode> treeNodes) {
        this.treeNodes = treeNodes;
    }

}
