package md.varoinform.model.entities;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 12:34 PM
 */
@Entity
@Table(name = "DB_streettitle")
public class StreetTitle extends Title {
    private Street street;

    public StreetTitle() {
    }

    public StreetTitle(Language language, String title, Street street) {
        super(language, title);
        setStreet(street);
    }

    @ManyToOne
    @JoinColumn(name = "street_id")
    public Street getStreet() {
        return street;
    }

    public void setStreet(Street street) {
        this.street = street;
    }
}

