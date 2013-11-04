package md.varoinform.model.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.*;

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

    @ManyToOne
    @JoinColumn(name = "enterprise_id")
    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    @Column(name = "postal_code")
    @Field
    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Column(name = "house_number")
    @Field
    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    @Column(name = "office_number")
    @Field
    public String getOfficeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber;
    }

    @ManyToOne
    @JoinColumn(name = "street_id")
    @IndexedEmbedded
    public Street getStreet() {
        return street;
    }

    public void setStreet(Street street) {
        this.street = street;
    }

    @ManyToOne
    @JoinColumn(name = "sector_id")
    @IndexedEmbedded
    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    @ManyToOne
    @JoinColumn(name = "town_id")
    @IndexedEmbedded
    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    @ManyToOne
    @JoinColumn(name = "region_id")
    @IndexedEmbedded
    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @ManyToOne
    @JoinColumn(name = "top_administrative_unit_id")
    @IndexedEmbedded
    public TopAdministrativeUnit getTopAdministrativeUnit() {
        return topAdministrativeUnit;
    }

    public void setTopAdministrativeUnit(TopAdministrativeUnit topAdministrativeUnit) {
        this.topAdministrativeUnit = topAdministrativeUnit;
    }

    @OneToMany
    @JoinTable(name = "DB_contact_email", joinColumns = @JoinColumn(name = "contact_id"), inverseJoinColumns = @JoinColumn(name = "id"))
    @IndexedEmbedded
    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }

    @OneToMany
    @JoinTable(name = "DB_contact_phone", joinColumns = @JoinColumn(name = "contact_id"), inverseJoinColumns = @JoinColumn(name = "id"))
    @IndexedEmbedded
    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    @OneToMany
    @JoinTable(name = "DB_contact_url", joinColumns = @JoinColumn(name = "contact_id"), inverseJoinColumns = @JoinColumn(name = "id"))
    @IndexedEmbedded
    public List<Url> getUrls() {
        return urls;
    }

    public void setUrls(List<Url> urls) {
        this.urls = urls;
    }

    @Override
    public String toString() {
        return   //"id=" + id +
                ", postalCode='" + postalCode + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", officeNumber='" + officeNumber + '\'' +
                ", street=" + street +
                ", sector=" + sector +
                ", town=" + town +
                ", region=" + region +
                ", topAdministrativeUnit=" + topAdministrativeUnit +
                ", emails=" + emails +
                ", phones=" + phones +
                ", urls=" + urls
                ;
    }
}
