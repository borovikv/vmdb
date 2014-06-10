package md.varoinform.model.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/8/14
 * Time: 9:26 AM
 */
@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name = "EXPORTED_DB.DB_GProduce")
public class GProduce {
    private Long id;
    private Enterprise enterprise;
    private Good good;
    private Boolean produce;

    public GProduce() {
    }



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
    @ContainedIn
    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    @ManyToOne
    @JoinColumn(name = "good_id")
    @IndexedEmbedded(includePaths = "titles.title")
    public Good getGood() {
        return good;
    }

    public void setGood(Good good) {
        this.good = good;
    }

    @Column(name = "is_produce")
    public Boolean getProduce() {
        return produce;
    }

    public void setProduce(Boolean produce) {
        this.produce = produce;
    }

    @Override
    public int hashCode() {
        return good.getTitles().hashCode();
    }

    @Override
    public String toString() {
        return "G2Produce{" +
                "good=" + good +
                ", produce=" + produce +
                '}';
    }
}
