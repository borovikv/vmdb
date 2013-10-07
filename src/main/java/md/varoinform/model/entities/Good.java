package md.varoinform.model.entities;

import javax.persistence.*;


/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 4:17 PM
 */
@Entity
@Table(name = "DB_good")
public class Good extends TitleContainer {
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }
}
