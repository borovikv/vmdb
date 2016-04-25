package md.varoinform.model.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/8/14
 * Time: 9:26 AM
 */
@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name = "enterprise_product")
public class EnterpriseProduct {
    private Integer id;
    private Enterprise enterprise;
    private Product product;
    private String productTypes;

    public EnterpriseProduct() {
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "enterprise_id")
    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    @ManyToOne
    @JoinColumn(name = "product_id")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Column(name = "productTypes")
    public String getProductType() {
        return productTypes;
    }

    public void setProductType(String productTypes) {
        this.productTypes = productTypes;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "EnterpriseProduct{" +
                "id=" + id +
                ", enterprise=" + enterprise.getId() +
                ", product=" + product +
                ", type=" + productTypes +
                '}';
    }
}
