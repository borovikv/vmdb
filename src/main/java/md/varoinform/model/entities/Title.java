package md.varoinform.model.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.*;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/4/13
 * Time: 9:34 AM
 */
@SuppressWarnings("UnusedDeclaration")
@MappedSuperclass
public class Title<T extends TitleContainer> implements TitleInterface{
    private Long id;
    private Language language;
    private String title;
    private T container;

    public Title() {
    }

    public Title(Language language, String title, T container) {
        setLanguage(language);
        setTitle(title);
        setContainer(container);
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
    @JoinColumn(name = "language_id")
    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    @Column(name = "title")
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Analyzer(definition = "customanalyzer")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @ManyToOne
    @JoinColumn(name = "container_id")
    public T getContainer() {
        return container;
    }

    public void setContainer(T container) {
        this.container = container;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
