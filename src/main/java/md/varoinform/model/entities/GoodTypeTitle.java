package md.varoinform.model.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/5/14
 * Time: 3:10 PM
 */
@Entity
@Table(name = "EXPORTED_DB.DB_GoodTypeTitle")
public class GoodTypeTitle extends Title<GoodType> {
}
