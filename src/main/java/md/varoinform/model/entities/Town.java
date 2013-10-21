package md.varoinform.model.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "DB_town")
public class Town extends TitleContainer<TownTitle> {
}
