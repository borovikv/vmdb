package md.varoinform.model.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/5/14
 * Time: 3:07 PM
 */
@Entity
@Table(name = "EXPORTED_DB.DB_GoodType")
public class GoodType extends TitleContainer<GoodTypeTitle> {
}
