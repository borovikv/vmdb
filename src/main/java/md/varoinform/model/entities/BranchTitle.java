package md.varoinform.model.entities;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/4/13
 * Time: 10:42 AM
 */
@Entity
@Table(name = "DB_branchtitle")
public class BranchTitle extends Title{
    private Branch branch;

    public BranchTitle() {
    }

    @ManyToOne
    @JoinColumn(name = "branch_id")
    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

}
