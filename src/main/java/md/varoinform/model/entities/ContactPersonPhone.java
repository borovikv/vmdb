package md.varoinform.model.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 4:31 PM
 */

@Entity
@Table(name = "ContactPersonPhone")
public class ContactPersonPhone {
    private Long id;
    private Long contactPersonID;
    private Long phoneId;

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "contactperson_id")
    public Long getContactPersonID() {
        return contactPersonID;
    }

    public void setContactPersonID(Long contactPersonID) {
        this.contactPersonID = contactPersonID;
    }

    @Column(name = "phone_id")
    public Long getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Long phoneId) {
        this.phoneId = phoneId;
    }
}
