package md.varoinform.model.entities;

import org.apache.solr.analysis.*;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 11:12 AM
 */
@SuppressWarnings("UnusedDeclaration")
@Entity
@Indexed
@AnalyzerDef(name = "customanalyzer",
        tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
        filters = {
                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                @TokenFilterDef(factory = StopFilterFactory.class, params = {@Parameter(name = "ignoreCase", value = "true"),
                        @Parameter(name = "words", value = "word.txt")}),
                @TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = {
                        @Parameter(name = "language", value = "Russian"),
                }),
                @TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = {
                        @Parameter(name = "language", value = "English")
                }),
                @TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = {
                        @Parameter(name = "language", value = "Romanian")
                })
        })
@Table(name = "EXPORTED_DB.DB_enterprise")
public class Enterprise extends TitleContainer<EnterpriseTitle> implements Serializable {
    private BusinessEntityType businessEntityType;
    private Integer creation;
    private Boolean foreignCapital;
    private Integer workplaces;
    private String logo;
    private String ypUrl;
    private Date checkDate;
    private Date lastChange;
    private List<Contact> contacts = new ArrayList<>();
    private List<Advertisement> advertisements = new ArrayList<>();
    private List<ContactPerson> contactPersons = new ArrayList<>();
    private List<Brand> brands = new ArrayList<>();
    private Set<G2Produce> goods = new HashSet<>();


    @ManyToOne
    @JoinColumn(name = "business_entity_id")
    @IndexedEmbedded
    public BusinessEntityType getBusinessEntityType() {
        return businessEntityType;
    }

    public void setBusinessEntityType(BusinessEntityType businessEntityType) {
        this.businessEntityType = businessEntityType;
    }

    @Column(name = "creation")
    public Integer getCreation() {
        return creation;
    }

    public void setCreation(Integer creation) {
        this.creation = creation;
    }

    @Column(name = "foreing_capital")
    public Boolean getForeignCapital() {
        return foreignCapital;
    }

    public void setForeignCapital(Boolean foreignCapital) {
        this.foreignCapital = foreignCapital;
    }

    @Column(name = "workplaces")
    public Integer getWorkplaces() {
        return workplaces;
    }

    public void setWorkplaces(Integer workplaces) {
        this.workplaces = workplaces;
    }

    @Column(name = "logo")
    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Column(name = "yp_url")
    public String getYpUrl() {
        return ypUrl;
    }

    public void setYpUrl(String ypUrl) {
        this.ypUrl = ypUrl;
    }

    @Column(name = "check_date")
    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    @Column(name = "last_change")
    public Date getLastChange() {
        return lastChange;
    }

    public void setLastChange(Date lastChange) {
        this.lastChange = lastChange;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "enterprise_id")
    @IndexedEmbedded
    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "enterprise_id")
    public List<Advertisement> getAdvertisements() {
        return advertisements;
    }

    public void setAdvertisements(List<Advertisement> advertisements) {
        this.advertisements = advertisements;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "enterprise_id")
    @IndexedEmbedded
    public List<ContactPerson> getContactPersons() {
        return contactPersons;
    }

    public void setContactPersons(List<ContactPerson> contactPersons) {
        this.contactPersons = contactPersons;
    }

    @ManyToMany
    @JoinTable(name = "EXPORTED_DB.DB_enterprise_brand", joinColumns = @JoinColumn(name = "enterprise_id"), inverseJoinColumns = @JoinColumn(name = "brand_id"))
    @IndexedEmbedded
    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    @OneToMany
    @JoinColumn(name = "enterprise_id")
    @IndexedEmbedded
    public Set<G2Produce> getGoods() {
        return goods;
    }

    public void setGoods(Set<G2Produce> goods) {
        this.goods = goods;
    }

    @Override
    public String toString() {
        return "Enterprise{" +
                "title =" + getTitles() +

                '}';
    }
}
