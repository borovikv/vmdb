package md.varoinform.model.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 4:20 PM
 */

@Entity
@Table(name = "EXPORTED_DB.DB_gproduce")
public class GProduce {
    private Long id;
    private Enterprise enterprise;
    private Good good;
    private Boolean produce;

    public GProduce() {
    }

    public GProduce(Enterprise enterprise, Good good, Boolean produce) {
        setEnterprise(enterprise);
        setGood(good);
        setProduce(produce);
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

    public Branch branch(){
        return good.getBranch();
    }

    @Override
    public String toString() {
        return "GProduce{" +
                "good=" + good +
                ", produce=" + produce +
                '}';
    }
}
