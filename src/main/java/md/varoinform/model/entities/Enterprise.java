package md.varoinform.model.entities;

import md.varoinform.controller.comparators.EnterpriseComparator;
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
        tokenizer = @TokenizerDef(factory = PatternTokenizerFactory.class,
                params = {
                    @Parameter(name = "pattern", value = "\\W")
        }),
        filters = {
                @TokenFilterDef(factory = ASCIIFoldingFilterFactory.class),
                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                @TokenFilterDef(factory = RemoveDuplicatesTokenFilterFactory.class),
                @TokenFilterDef(factory = WordDelimiterFilterFactory.class),
                @TokenFilterDef(factory = EdgeNGramFilterFactory.class,
                        params = {
                                @Parameter(name = "minGramSize", value = "3"),
                                @Parameter(name = "maxGramSize", value = "15"),
                                @Parameter(name = "side", value = "front")
                        } ),
                @TokenFilterDef(factory = StopFilterFactory.class, params = {@Parameter(name = "ignoreCase", value = "true"),
                        @Parameter(name = "words", value = "word.txt")}),
                @TokenFilterDef(factory = SynonymFilterFactory.class,
                        params = {
                            @Parameter(name = "ignoreCase", value = "true"),
                            @Parameter(name = "synonyms", value = "synonyms.txt")
                        }),
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
public class Enterprise extends TitleContainer<EnterpriseTitle> implements Serializable, Comparable<Enterprise> {
    private BusinessEntityType businessEntityType;
    private Integer creation;
    private Boolean foreignCapital;
    private Integer workplaces;
    private String logo;
    private Date checkDate;
    private Date lastChange;
    private List<Contact> contacts = new ArrayList<>();
    private List<Advertisement> advertisements = new ArrayList<>();
    private List<ContactPerson> contactPersons = new ArrayList<>();
    private List<Brand> brands = new ArrayList<>();
    private Set<GProduce> goods = new HashSet<>();
    private static final EnterpriseComparator comparator = new EnterpriseComparator();


    @ManyToOne
    @JoinColumn(name = "business_entity_id")
    @IndexedEmbedded(includePaths = { "titles.title" })
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
    @IndexedEmbedded(includePaths = { "postalCode", "houseNumber", "officeNumber",
            "street.titles.title", "sector.titles.title", "town.titles.title",
            "region.titles.title",
            "emails.email", "phones.phone", "urls.url" })
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
    @IndexedEmbedded(includePaths = {"person.titles.title", "phones.phone"})
    public List<ContactPerson> getContactPersons() {
        return contactPersons;
    }

    public void setContactPersons(List<ContactPerson> contactPersons) {
        this.contactPersons = contactPersons;
    }

    @ManyToMany
    @JoinTable(name = "EXPORTED_DB.DB_enterprise_brand", joinColumns = @JoinColumn(name = "enterprise_id"), inverseJoinColumns = @JoinColumn(name = "brand_id"))
    @IndexedEmbedded(includePaths = {"title"})
    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    @OneToMany
    @JoinColumn(name = "enterprise_id")
    @IndexedEmbedded(includePaths = {"good.titles.title"})
    public Set<GProduce> getGoods() {
        return goods;
    }

    public void setGoods(Set<GProduce> goods) {
        this.goods = goods;
    }

    @Override
    public String toString() {
        return "Enterprise{" +
                "title =" + getTitles() +

                '}';
    }

    @Override
    public int compareTo(Enterprise o) {
        return comparator.compare(this, o);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Enterprise && id.equals(((Enterprise) obj).id);
    }
}
