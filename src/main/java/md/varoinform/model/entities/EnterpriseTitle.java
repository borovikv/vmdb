package md.varoinform.model.entities;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 11:26 AM
 */
@Entity
@Table(name = "DB_enterprisetitle")
public class EnterpriseTitle extends Title {
    private Enterprise enterprise;

    public EnterpriseTitle() {
    }

    public EnterpriseTitle(Language language, String title, Enterprise enterprise) {
        super(language, title);
        setEnterprise(enterprise);
    }

    @ManyToOne
    @JoinColumn(name = "enterprise_id")
    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }
}
