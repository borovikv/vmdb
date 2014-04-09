package md.varoinform.model.entities;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 4:20 PM
 */

@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name = "EXPORTED_DB.DB_gproduce")
public class GProduce {
    private Long id;
    private Enterprise enterprise;
    private Long good;
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
    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    @Column(name = "good_id")
    public Long getGood() {
        return good;
    }

    public void setGood(Long good) {
        this.good = good;
    }

    @Column(name = "is_produce")
    public Boolean getProduce() {
        return produce;
    }

    public void setProduce(Boolean produce) {
        this.produce = produce;
    }

    public TreeNode branch(){
        //return good.getTreeNodes();
        return null;
    }

    @Override
    public String toString() {
        return "GProduce{" +
                "good=" + good +
                ", produce=" + produce +
                '}';
    }
}
