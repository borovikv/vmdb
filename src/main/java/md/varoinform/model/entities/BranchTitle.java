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
public class BranchTitle extends Title<Branch>{
    public BranchTitle() {
    }

    public BranchTitle(Language language, String title, Branch branch) {
        super(language, title, branch);
    }

}
