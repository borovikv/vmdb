package md.varoinform.model.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "EXPORTED_DB.DB_sector")
public class Sector extends TitleContainer<SectorTitle> {
    public Sector() {
    }

    public Sector(List<SectorTitle> titles) {
        setTitles(titles);
    }
}
