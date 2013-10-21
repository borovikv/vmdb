package md.varoinform.model.entities;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 1:51 PM
 */
@Entity
@Table(name = "DB_topadministrativeunittitle")
public class TopAdministrativeUnitTitle extends Title {
    private TopAdministrativeUnit topadministrativeunit;

    public TopAdministrativeUnitTitle() {
    }

    public TopAdministrativeUnitTitle(Language language, String title, TopAdministrativeUnit topadministrativeunit) {
        super(language, title);
        setTopAdministrativeUnit(topadministrativeunit);
    }

    @ManyToOne
    @JoinColumn(name = "topadministrativeunit_id")
    public TopAdministrativeUnit getTopAdministrativeUnit() {
        return topadministrativeunit;
    }

    public void setTopAdministrativeUnit(TopAdministrativeUnit topadministrativeunit) {
        this.topadministrativeunit = topadministrativeunit;
    }
}

