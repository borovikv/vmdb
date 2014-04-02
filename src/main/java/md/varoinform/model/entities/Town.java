package md.varoinform.model.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "EXPORTED_DB.DB_town")
public class Town extends TitleContainer<TownTitle> {
}
