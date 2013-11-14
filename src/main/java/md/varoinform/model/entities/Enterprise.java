package md.varoinform.model.entities;

import org.apache.solr.analysis.*;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 11:12 AM
 */
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
@Table(name = "DB_enterprise")
public class Enterprise extends TitleContainer<EnterpriseTitle>{
    private Integer idno;
    private BusinessEntityType businessEntityType;
    private Date creation;
    private Boolean foreingCapital;
    private Integer workplaces;
    private String logo;
    private String ypUrl;
    private Date checkDate;
    private Date lastChange;
    private List<Contact> contacts = new ArrayList<>();
    private List<Advertisement> advertisements = new ArrayList<>();
    private List<ContactPerson> contactPersons = new ArrayList<>();
    private List<Brand> brands = new ArrayList<>();
    private List<GProduce> goods = new ArrayList<>();
    private List<Branch> branches;

    @Column(name = "idno")
    public Integer getIdno() {
        return idno;
    }

    public void setIdno(Integer idno) {
        this.idno = idno;
    }

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
    public Date getCreation() {
        return creation;
    }

    public void setCreation(Date creation) {
        this.creation = creation;
    }

    @Column(name = "foreing_capital")
    public Boolean getForeingCapital() {
        return foreingCapital;
    }

    public void setForeingCapital(Boolean foreingCapital) {
        this.foreingCapital = foreingCapital;
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
    @JoinTable(name = "DB_enterprise_brand", joinColumns = @JoinColumn(name = "enterprise_id"), inverseJoinColumns = @JoinColumn(name = "brand_id"))
    @IndexedEmbedded
    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "enterprise_id")
    @IndexedEmbedded
    public List<GProduce> getGoods() {
        return goods;
    }

    public void setGoods(List<GProduce> goods) {
        this.goods = goods;
    }

    public List<Branch> branches() {
        if (branches == null){
            branches = new ArrayList<>();
            for (GProduce good : goods) {
                branches.add(good.branch());
            }
        }
        return branches;
    }

    @Override
    public String toString() {
        return "Enterprise{" +
                "title =" + getTitles() +
                ", businessEntityType=" + businessEntityType +
                ", creation=" + creation +
                ", foreingCapital=" + foreingCapital +
                ", workplaces=" + workplaces +
                ", logo='" + logo + '\'' +
                ", ypUrl='" + ypUrl + '\'' +
                ", checkDate=" + checkDate +
                ", lastChange=" + lastChange +
                ", contacts=" + contacts +
                ", advertisements=" + advertisements +
                ", contactPersons=" + contactPersons +
                ", brands=" + brands +
                ", goods=" + goods +
                ", branches=" + branches +
                '}';
    }
}
