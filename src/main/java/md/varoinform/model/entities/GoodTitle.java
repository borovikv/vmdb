package md.varoinform.model.entities;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 4:16 PM
 */
@Entity
@Table(name = "EXPORTED_DB.DB_goodtitle")
public class GoodTitle extends Title<Good> {
    public GoodTitle() {
    }

    public GoodTitle(Language language, String title, Good good) {
        super(language, title, good);
    }
}

