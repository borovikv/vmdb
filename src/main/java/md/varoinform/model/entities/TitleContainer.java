package md.varoinform.model.entities;

import org.hibernate.annotations.GenericGenerator;

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

    @OneToMany
    @JoinColumn(name = "container_id")
    public List<T> getTitles() {
        return titles;
    }

    public void setTitles(List<T> titles) {
        this.titles = titles;
    }

    public String getTitle(Language lang){
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

}
