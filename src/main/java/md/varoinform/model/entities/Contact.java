package md.varoinform.model.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/8/13
 * Time: 10:54 AM
 */

@Entity
@Table(name = "DB_contact")
public class Contact {
    private Long id;
    private Enterprise enterprise;
    private String postalCode;
    private String houseNumber;
    private String officeNumber;
    private Street street;
    private Sector sector;
    private Town town;
    private Region region;
    private TopAdministrativeUnit topAdministrativeUnit;
    private List<Email> emails = new ArrayList<>();
    private List<Phone> phones = new ArrayList<>();
    private List<Url> urls = new ArrayList<>();


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
}
