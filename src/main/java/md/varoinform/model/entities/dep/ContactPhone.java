package md.varoinform.model.entities.dep;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 11:50 AM
 */
@Entity
@Table(name = "DB_contact_phone")
public class ContactPhone {
    private Long id;
    private Long contactID;
    private Long phoneID;

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

    @Column(name = "contact_id")
    public Long getContactID() {
        return contactID;
    }

    public void setContactID(Long contactID) {
        this.contactID = contactID;
    }

    @Column(name = "phone_id")
    public Long getPhoneID() {
        return phoneID;
    }

    public void setPhoneID(Long phoneID) {
        this.phoneID = phoneID;
    }
}
