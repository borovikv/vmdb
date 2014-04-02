package md.varoinform.model.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/2/13
 * Time: 4:07 PM
 */
@Entity
@Table(name = "DB_branch")
public class Branch extends TitleContainer<BranchTitle> {
    private Branch parent;
    private List<Branch> children = new ArrayList<>();

    public Branch() {
    }

    public Branch(Branch parent) {
        setParent(parent);
    }

    @ManyToOne(cascade = CascadeType.ALL)
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

}
