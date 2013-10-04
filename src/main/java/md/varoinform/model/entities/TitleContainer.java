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
public class TitleContainer {
    protected Long id;
    private List<BranchTitle> titles = new ArrayList<>();

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
    @JoinColumn(name = "branch_id")
    public List<BranchTitle> getTitles() {
        return titles;
    }

    public void setTitles(List<BranchTitle> titles) {
        this.titles = titles;
    }

    public String getTitle(Language lang){
        for (BranchTitle title : titles) {
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
