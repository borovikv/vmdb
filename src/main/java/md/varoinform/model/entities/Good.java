package md.varoinform.model.entities;

import javax.persistence.*;


/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 4:17 PM
 */
@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name = "EXPORTED_DB.DB_good")
public class Good extends TitleContainer<GoodTitle> {
    private Long branchID;

    public Good() {
    }

    @Column(name = "branch_id")
    public Long getBranchID() {
        return branchID;
    }

    public void setBranchID(Long branchID) {
        this.branchID = branchID;
    }


    @Override
    public String toString() {
        return "id = " + getId() + " titles = " + getTitles() + " branch_id = " + branchID;
    }
}
