package md.varoinform.model.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 4:39 PM
 */

@Entity
@Table(name = "EXPORTED_DB.DB_advertisement")
public class Advertisement {
    private Long id;
    private Enterprise enterprise;
    private String imagePath;
    private List<AdvertisementText> texts = new ArrayList<>();

    public Advertisement() {
    }

    public Advertisement(Enterprise enterprise, String imagePath) {
        setEnterprise(enterprise);
        setImagePath(imagePath);
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

    @ManyToOne
    @JoinColumn(name = "enterprise_id")
    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    @Column(name = "image")
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "advertisement_id")
    public List<AdvertisementText> getTexts() {
        return texts;
    }

    public void setTexts(List<AdvertisementText> texts) {
        this.texts = texts;
    }

    public String getText(Language lang){
        for (AdvertisementText text : texts) {
            if (text.getLanguage().equals(lang)){
                return  text.getText();
            }
        }
        if (texts.size() > 0){
            return texts.get(0).getText();
        }
        return null;
    }

}
