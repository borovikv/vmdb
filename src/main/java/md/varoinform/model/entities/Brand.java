package md.varoinform.model.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 11:05 AM
 */
@Entity
@Table(name = "EXPORTED_DB.DB_brand")
public class Brand {
    private Long id;
    private String title;

    public Brand() {
    }

    public Brand(String title) {
        setTitle(title);
    }

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
    @Field
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
