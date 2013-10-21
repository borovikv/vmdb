package md.varoinform.model.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "DB_region")
public class Region extends TitleContainer<RegionTitle> {
}
