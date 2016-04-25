package md.varoinform.model.entities;


import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by vladimir on 22.04.16.
 *
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "product")
public class Product extends Title implements Serializable{
    private Product parent;
    private List<Product> children;
    private List<Enterprise> enterprises;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    public Product getParent() {
        return parent;
    }

    public void setParent(Product parent) {
        this.parent = parent;
    }

    @OneToMany
    @JoinColumn(name = "parent_id")
    public List<Product> getChildren() {
        return children;
    }

    public void setChildren(List<Product> children) {
        this.children = children;
    }

//    @ManyToMany
//    @JoinTable(name = "enterprise_product", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "enterprise_id"))
    public List<Enterprise> gEnterprises() {
        return enterprises;
    }

    public void setEnterprises(List<Enterprise> enterprises) {
        this.enterprises = enterprises;
    }
}
