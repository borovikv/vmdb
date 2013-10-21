package md.varoinform.model.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 4:29 PM
 */
@Entity
@Table(name = "DB_person")
public class Person extends TitleContainer<PersonTitle> {
}
