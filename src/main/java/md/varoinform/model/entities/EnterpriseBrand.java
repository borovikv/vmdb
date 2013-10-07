package md.varoinform.model.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 11:11 AM
 */
@Entity
@Table(name = "DB_enterprise_brand")
public class EnterpriseBrand {
    private Long id;
    private Long enterpriseID;
    private Long brandId;

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

    @Column(name = "enterprise_id")
    public Long getEnterpriseID() {
        return enterpriseID;
    }

    public void setEnterpriseID(Long enterpriseID) {
        this.enterpriseID = enterpriseID;
    }

    @Column(name = "brand_id")
    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }
}
