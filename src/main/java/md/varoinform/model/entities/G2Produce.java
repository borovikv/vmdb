package md.varoinform.model.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/8/14
 * Time: 9:26 AM
 */
@Entity
@Table(name = "EXPORTED_DB.DB_g2produce")
public class G2Produce {
    private Long id;
    private Enterprise enterprise;
    private Good2 good;
    private Boolean produce;

    public G2Produce() {
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
    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    @ManyToOne
    @JoinColumn(name = "good_id")
    @IndexedEmbedded
    public Good2 getGood() {
        return good;
    }

    public void setGood(Good2 good) {
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
    public String toString() {
        return "GProduce{" +
                "good=" + good +
                ", produce=" + produce +
                '}';
    }
}
