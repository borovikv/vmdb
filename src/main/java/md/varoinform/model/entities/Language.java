package md.varoinform.model.entities;

/**
 * User: vladimir borovic
 * Date: 10/2/13
 * Time: 3:41 PM
 */

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "EXPORTED_DB.DB_language")
public class Language {
    private Long id;
    private String title;

    public Language() {
    }

    public Language(String title) {
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

    @Column(name = "title", nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public boolean equals(Object obj) {
        return ((obj instanceof Language)) && ((Language)obj).id.equals(id);
    }

    @Override
    public int hashCode() {
        int result = 17;
        long id = this.id;
        int c = (int)(id^(id >>> 32));
        result = 31 * result + c;
        return  result;
    }
}
