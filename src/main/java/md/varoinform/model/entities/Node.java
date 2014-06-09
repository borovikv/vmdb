package md.varoinform.model.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 6/6/14
 * Time: 1:38 PM
 */
@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name = "EXPORTED_DB.DB_Node")
public class Node extends TitleContainer<NodeTitle>  implements Serializable{
    private List<Node> children = new ArrayList<>();
    private List<Enterprise> enterprises = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "EXPORTED_DB.DB_Arc", joinColumns = @JoinColumn(name = "tail_id"), inverseJoinColumns = @JoinColumn(name = "head_id"))
    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    @ManyToMany
    @JoinTable(name = "EXPORTED_DB.DB_Node_Enterprise", joinColumns = @JoinColumn(name = "node_id"), inverseJoinColumns = @JoinColumn(name = "enterprise_id"))
    public List<Enterprise> getEnterprises() {
        return enterprises;
    }

    public void setEnterprises(List<Enterprise> enterprises) {
        this.enterprises = enterprises;
    }
}
