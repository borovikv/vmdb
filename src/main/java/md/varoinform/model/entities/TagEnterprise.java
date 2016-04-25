package md.varoinform.model.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 3/27/14
 * Time: 4:41 PM
 */

@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name = "tag_enterprise")
public class TagEnterprise {
    @EmbeddedId
    private Id id = new Id();

    @ManyToOne
    @JoinColumn(name = "tag_id", insertable = false, updatable = false)
    private Tag tag;

    @ManyToOne
    @JoinColumn(name = "enterprise_id", insertable = false, updatable = false)
    private Enterprise enterprise;

    @Embeddable
    public static class Id implements Serializable {
        @Column(name = "tag_id")
        private Integer tagId;

        @Column(name = "enterprise_id")
        private Integer enterpriseId;

        public Id() {}

        public Id(Integer tagId, Integer enterpriseId) {
            this.tagId = tagId;
            this.enterpriseId = enterpriseId;
        }


    }
}
