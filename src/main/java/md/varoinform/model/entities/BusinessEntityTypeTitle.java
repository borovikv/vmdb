package md.varoinform.model.entities;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 10:54 AM
 */
@Entity
@Table(name = "DB_businessentitytype")
public class BusinessEntityTypeTitle extends Title {
    private BusinessEntityType businessEntityType;

    @ManyToOne
    @JoinColumn(name = "business_entity_id")
    public BusinessEntityType getBusinessEntityType() {
        return businessEntityType;
    }

    public void setBusinessEntityType(BusinessEntityType businessEntityType) {
        this.businessEntityType = businessEntityType;
    }
}
