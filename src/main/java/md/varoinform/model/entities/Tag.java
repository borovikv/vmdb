package md.varoinform.model.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/29/13
 * Time: 9:45 AM
 */

@Entity
@Table(name = "DB_tag")
public class Tag {
    private Long id;
    private String title;
    private Set<Enterprise> enterprises;

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

    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @ManyToMany
    @JoinTable(name = "EXPORTED_DB.DB_tag_enterprise", joinColumns = @JoinColumn(name = "tag_id"), inverseJoinColumns = @JoinColumn(name = "enterprise_id"))
    public Set<Enterprise> getEnterprises() {
        return enterprises;
    }

    public void setEnterprises(Set<Enterprise> enterprises) {
        this.enterprises = enterprises;
    }

    @Override
    public String toString() {
        return title;
    }

    public void removeAll(Collection<Enterprise> collection){
        enterprises.removeAll(collection);
    }
}
