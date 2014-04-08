package md.varoinform.model.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/4/14
 * Time: 10:03 AM
 */
@Entity
@Table(name = "EXPORTED_DB.DB_Branch")
public class Branch {
    @EmbeddedId
    private Id id = new Id();

    public Long id() {
        return id.id;
    }
    public Long parentId(){
        return id.parent;
    }

    public Branch() {
    }

    @Embeddable
    public static class Id implements Serializable {
        @Column(name = "id")
        private Long id;

        @Column(name = "parent_id")
        private Long parent;

        public Id() {}

        public Id(Long id, Long parent) {
            this.id = id;
            this.parent = parent;
        }

    }
}
