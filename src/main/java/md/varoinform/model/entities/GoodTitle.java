package md.varoinform.model.entities;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 4:16 PM
 */
@Entity
@Table(name = "DB_goodtitle")
public class GoodTitle extends Title {
    private Good good;

    public GoodTitle() {
    }

    public GoodTitle(Language language, String title, Good good) {
        super(language, title);
        setGood(good);
    }

    @ManyToOne
    @JoinColumn(name = "good_id")
    public Good getGood() {
        return good;
    }

    public void setGood(Good good) {
        this.good = good;
    }
}

