package md.varoinform.model.entities;

import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 10:54 AM
 */
@Entity
@Table(name = "DB_businessentitytype")
public class BusinessEntityTypeTitle extends Title<BusinessEntityType> {
    public BusinessEntityTypeTitle() {
    }

    public BusinessEntityTypeTitle(Language language, String title, BusinessEntityType businessEntityType) {
        super(language, title, businessEntityType);
    }
}
