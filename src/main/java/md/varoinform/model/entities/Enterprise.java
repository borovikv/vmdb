package md.varoinform.model.entities;

import org.apache.solr.analysis.*;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
        tokenizer = @TokenizerDef(factory = WhitespaceTokenizerFactory.class,
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
@Table(name = "enterprise")
public class Enterprise extends Title implements Serializable {
    private Integer uid;
    private String idno;
    private Short creationYear;
    private Short numberOfJobs;
    private Date lastChange;
    private Boolean tva;

    private String persons;
    private String brands;

    private String location;
    private String urls;
    private String emails;
    private String phones;
    private String faxes;
    private String mob;

    private Set<EnterpriseProduct> products = new HashSet<>();


    @Column(name = "uid")
    @Field(analyze = Analyze.NO)
    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    @Column(name = "idno")
    @Field(analyze = Analyze.NO)
    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    @Column(name = "creation_year")
    public Short getCreationYear() {
        return creationYear;
    }

    public void setCreationYear(Short creationYear) {
        this.creationYear = creationYear;
    }


    @Column(name = "number_of_jobs")
    public Short getNumberOfJobs() {
        return numberOfJobs;
    }

    public void setNumberOfJobs(Short numberOfJobs) {
        this.numberOfJobs = numberOfJobs;
    }

    @Column(name = "last_change")
    public Date getLastChange() {
        return lastChange;
    }

    public void setLastChange(Date lastChange) {
        this.lastChange = lastChange;
    }

    @Column(name = "persons")
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Analyzer(definition = "customanalyzer")
    public String getPersons() {
        return persons;
    }

    public void setPersons(String persons) {
        this.persons = persons;
    }

    @Column(name = "brands")
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Analyzer(definition = "customanalyzer")
    public String getBrands() {
        return brands;
    }

    public void setBrands(String brands) {
        this.brands = brands;
    }

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "enterprise_id", updatable = false)
    @IndexedEmbedded(includePaths = {"product.titles.title"})
    public Set<EnterpriseProduct> getProducts() {
        return products;
    }

    public void setProducts(Set<EnterpriseProduct> products) {
        this.products = products;
    }

    @Column(name = "emails")
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Analyzer(definition = "customanalyzer")
    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    @Column(name = "location")
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Analyzer(definition = "customanalyzer")
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    @Column(name = "tva")
    public Boolean getTva() {
        return tva;
    }

    public void setTva(Boolean tva) {
        this.tva = tva;
    }

    @Column(name = "urls")
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Analyzer(definition = "customanalyzer")
    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    @Override
    public String toString() {
        return "Enterprise{" +
                "title =" + title +
                '}';
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Enterprise && getId().equals(((Enterprise) obj).getId());
    }

    @Column(name = "phones")
    @Field(analyze = Analyze.YES)
    public String getPhones() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }

    @Column(name = "faxes")
    @Field(analyze = Analyze.YES)
    public String getFaxes() {
        return faxes;
    }

    public void setFaxes(String faxes) {
        this.faxes = faxes;
    }

    @Column(name = "mob")
    @Field(analyze = Analyze.YES)
    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }



    public Object getValue(String name) {
        try {
            Method[] methods = this.getClass().getMethods();
            for (Method m :methods) {

                if (m.getName().startsWith("get") && m.getName().toLowerCase().contains(name.toLowerCase())){
                    return m.invoke(this);
                }
            }

        } catch ( InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> getColumnType(String name) {
        return String.class;
    }

    public static List<String> getFields() {
        return Arrays.asList("idno", "creationYear", "numberOfJobs", "lastChange", "tva", "persons", "brands", "location", "urls", "emails", "phones", "faxes", "mob", "products", "title");
    }
}
