package md.varoinform.model.entities;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 4:27 PM
 */
@Entity
@Table(name = "DB_positiontitle")
public class PositionTitle extends Title<Position> {
    public PositionTitle() {
    }

    public PositionTitle(Language language, String title, Position position) {
        super(language, title, position);
    }
}

