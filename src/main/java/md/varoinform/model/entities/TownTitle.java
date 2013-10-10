package md.varoinform.model.entities;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 1:47 PM
 */
@Entity
@Table(name = "DB_towntitle")
public class TownTitle extends Title {
    private Town town;

    public TownTitle() {
    }

    public TownTitle(Language language, String title, Town town) {
        super(language, title);
        setTown(town);
    }

    @ManyToOne
    @JoinColumn(name = "town_id")
    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }
}

