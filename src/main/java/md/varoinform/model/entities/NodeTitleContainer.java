package md.varoinform.model.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/4/14
 * Time: 9:35 AM
 */

@Entity
@Table(name = "EXPORTED_DB.DB_NodeTitleContainer")
public class NodeTitleContainer extends TitleContainer<NodeTitle> {
    public NodeTitleContainer() {
    }
}
