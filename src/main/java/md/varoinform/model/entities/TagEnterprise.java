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
@Table(name = "DB_tag_enterprise")
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
        private Long tagId;

        @Column(name = "enterprise_id")
        private Long enterpriseId;

        public Id() {}

        public Id(Long categoryId, Long itemId) {
            this.tagId = categoryId;
            this.enterpriseId = itemId;
        }


    }
}
