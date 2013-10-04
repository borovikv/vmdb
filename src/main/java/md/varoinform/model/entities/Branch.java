package md.varoinform.model.entities;

import org.hibernate.annotations.GenericGenerator;

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
public class Branch {
    private Long id;
    private Branch parent;
    private List<Branch> children = new ArrayList<Branch>();

    public Branch() {
    }

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

    public void setChildren(List<Branch> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "Branch{" +
                "id=" + id +
                ", parent=" + parent +
                '}';
    }

    public String name() {
        return toString();
    }
}
