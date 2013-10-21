package md.varoinform.model.entities;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 1:49 PM
 */
@Entity
@Table(name = "DB_regiontitle")
public class RegionTitle extends Title {
    private Region region;

    public RegionTitle() {
    }

    public RegionTitle(Language language, String title, Region region) {
        super(language, title);
        setRegion(region);
    }

    @ManyToOne
    @JoinColumn(name = "region_id")
    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
}

