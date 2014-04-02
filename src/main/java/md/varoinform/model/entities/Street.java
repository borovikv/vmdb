package md.varoinform.model.entities;

import javax.persistence.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 12:34 PM
 */
@Entity
@Table(name = "EXPORTED_DB.DB_street")
public class Street extends TitleContainer<StreetTitle> {
    public Street() {}
    public Street(List<StreetTitle> titles) {
        setTitles(titles);
    }
}
