package md.varoinform.model.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 4:27 PM
 */
@Entity
@Table(name = "EXPORTED_DB.DB_position")
public class Position extends TitleContainer<PositionTitle> {
}
