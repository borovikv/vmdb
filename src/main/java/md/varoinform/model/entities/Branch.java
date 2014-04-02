package md.varoinform.model.entities;

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
@Table(name = "EXPORTED_DB.DB_branch")
public class Branch extends TitleContainer<BranchTitle> implements Serializable{
    private Branch parent;
    private List<Branch> children = new ArrayList<>();
    @EmbeddedId
    private Id branchId = new Id();

    public Branch() {
    }

    public Branch(Branch parent) {
        setParent(parent);
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "parent_id")
    public Branch getParent() {
        return parent;
    }

    public void setParent(Branch parent) {
        this.parent = parent;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_id")
    public List<Branch> getChildren() {
        return children;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setChildren(List<Branch> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Branch branch = (Branch) o;

        return getId().equals(branch.getId());
    }

    @Override
    public int hashCode() {
        int result = parent.hashCode();
        result = 31 * result + children.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Branch{" +
                "id=" + id +
                ", parent=" + parent +
                ", titles=" + getTitles() +
                '}';
    }


    @Embeddable
    public static class Id implements Serializable {
        @Column(name = "id")
        private Long id;

        @Column(name = "parent_id")
        private Long parentId;

        public Id() {}

        public Id(Long id, Long parentId) {
            this.id = id;
            this.parentId = parentId;
        }


    }

}
