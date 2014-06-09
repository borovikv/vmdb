package md.varoinform.model.entities;


import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/4/14
 * Time: 1:58 PM
 */
@Entity
@Table(name = "EXPORTED_DB.DB_GoodTitle")
public class GoodTitle extends Title<Good> {
}
