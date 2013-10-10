package md.varoinform.model.entities;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 1:43 PM
 */
@Entity
@Table(name = "DB_sectortitle")
public class SectorTitle extends Title {
    private Sector sector;

    public SectorTitle() {
    }

    public SectorTitle(Language language, String title, Sector sector) {
        super(language, title);
        setSector(sector);
    }

    @ManyToOne
    @JoinColumn(name = "sector_id")
    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }
}

