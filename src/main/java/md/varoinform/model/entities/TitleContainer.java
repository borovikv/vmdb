package md.varoinform.model.entities;

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
    private List<BranchTitle> titles = new ArrayList<>();

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
