package md.varoinform.model.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.Field;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 2:04 PM
 */

@Entity
@Table(name = "EXPORTED_DB.DB_url")
public class Url {
    private Long id;
    private String url;

    public Url() {
    }

    public Url(String url) {
        setUrl(url);
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

    @Column(name = "url")
    @Field
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return url;
    }
}
