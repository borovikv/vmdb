package md.varoinform.model.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/4/13
 * Time: 2:32 PM
 */
@MappedSuperclass
public class TitleContainer<T extends Title> {
    protected Long id;
    private List<T> titles = new ArrayList<>();

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

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "container_id")
    @IndexedEmbedded
    public List<T> getTitles() {
        return titles;
    }

    public void setTitles(List<T> titles) {
        this.titles = titles;
    }

    public String title(Language lang){
        for (T title : titles) {
            if (title.getLanguage().equals(lang)){
                return title.getTitle();
            }
        }
        if (titles.size() > 0){
            return titles.get(0).getTitle();
        }
        return null;
    }

    @Override
    public String toString() {
        return "titles=" + titles;
    }
}
