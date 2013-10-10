package md.varoinform.model.entities;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 4:28 PM
 */
@Entity
@Table(name = "DB_persontitle")
public class PersonTitle extends Title {
    private Person person;

    public PersonTitle() {
    }

    public PersonTitle(Language language, String title, Person person) {
        super(language, title);
        setPerson(person);
    }

    @ManyToOne
    @JoinColumn(name = "person_id")
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}

