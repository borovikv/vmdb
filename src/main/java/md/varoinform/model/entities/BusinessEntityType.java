package md.varoinform.model.entities;

import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 10:54 AM
 */
@Entity
@Table(name = "EXPORTED_DB.DB_businessentitytype")
public class BusinessEntityType extends TitleContainer<BusinessEntityTypeTitle> {
}
