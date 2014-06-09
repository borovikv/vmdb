package md.varoinform.model.entities;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/4/14
 * Time: 9:37 AM
 */

@Entity
@Table(name = "EXPORTED_DB.DB_NodeTitle")
public class NodeTitle extends Title<Node>{
    public NodeTitle() {
    }
}
