package md.varoinform.model.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 4:20 PM
 */

@Entity
@Table(name = "DB_gproduce")
public class GProduce {
    private Long id;
    private Enterprise enterprise;
    private Good good;
    private Boolean isProduce;

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
    public Good getGood() {
        return good;
    }

    public void setGood(Good good) {
        this.good = good;
    }

    @Column(name = "is_produce")
    public Boolean getProduce() {
        return isProduce;
    }

    public void setProduce(Boolean produce) {
        isProduce = produce;
    }

    public Branch branch(){
        return good.getBranch();
    }
}