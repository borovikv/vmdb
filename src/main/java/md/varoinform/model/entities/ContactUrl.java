package md.varoinform.model.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 11:49 AM
 */
@Entity
@Table(name = "DB_contact_url")
public class ContactUrl {
    private Long id;
    private Long contactID;
    private Long urlID;

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

    @Column(name = "url_id")
    public Long getUrlID() {
        return urlID;
    }

    public void setUrlID(Long urlID) {
        this.urlID = urlID;
    }
}
