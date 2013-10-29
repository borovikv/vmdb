package md.varoinform.model.entities;

import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 4:17 PM
 */
@Entity
@Table(name = "DB_good")
public class Good extends TitleContainer<GoodTitle> {
    private Branch branch;

    public Good() {
    }

    public Good(Branch branch) {
        setBranch(branch);
    }

    @ManyToOne
    @JoinColumn(name = "branch_id")
    @IndexedEmbedded
    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    @Override
    public String toString() {
        return "id = " + getId() + " titles = " + getTitles() + " branch = " + branch;
    }
}
