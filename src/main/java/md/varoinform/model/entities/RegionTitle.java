package md.varoinform.model.entities;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 1:49 PM
 */
@Entity
@Table(name = "EXPORTED_DB.DB_regiontitle")
public class RegionTitle extends Title<Region> {
    public RegionTitle() {
    }

    public RegionTitle(Language language, String title, Region region) {
        super(language, title, region);
    }
}

