package md.varoinform.model.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 4:34 PM
 */
@Entity
@Table(name = "EXPORTED_DB.DB_contactperson")
public class ContactPerson {
    private Long id;
    private Enterprise enterprise;
    private Person person;
    private Position position;
    private List<Phone> phones = new ArrayList<>();

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

    @ManyToOne
    @JoinColumn(name = "enterprise_id")
    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    @ManyToOne
    @JoinColumn(name = "person_id")
    @IndexedEmbedded
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @ManyToOne
    @JoinColumn(name = "position_id")
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @OneToMany
    @JoinTable(name = "EXPORTED_DB.DB_contactperson_phones", joinColumns = @JoinColumn(name = "contactperson_id"), inverseJoinColumns = @JoinColumn(name = "id"))
    @IndexedEmbedded
    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }
}
