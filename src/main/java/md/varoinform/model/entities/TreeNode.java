package md.varoinform.model.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/2/13
 * Time: 4:07 PM
 */
@Entity
@Table(name = "EXPORTED_DB.DB_TreeNode")
public class TreeNode implements Serializable{
    private Long id;
    private Long parent;
    private NodeTitleContainer title;

    private List<TreeNode> children = new ArrayList<>();

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "title_id")
    public NodeTitleContainer getTitle() {
        return title;
    }

    public void setTitle(NodeTitleContainer title) {
        this.title = title;
    }



    public TreeNode() {
    }


    //@ManyToOne(optional = false)
    @Column(name = "parent_id")
    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    //@OneToMany(cascade = CascadeType.ALL)
    //@JoinColumn(name = "parent_id")
    /*public List<TreeNode> getChildren() {
        return children;
    }
    @SuppressWarnings("UnusedDeclaration")
    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TreeNode treeNode = (TreeNode) o;

        return id.equals(treeNode.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "id=" + id +
                ", parent=" + parent +
                ", titles=" + getTitle() +
                '}';
    }


    public String title(Language currentLanguage) {

        return "" + getTitle();
    }
}
