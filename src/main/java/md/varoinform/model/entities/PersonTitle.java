package md.varoinform.model.entities;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 4:28 PM
 */
@Entity
@Table(name = "EXPORTED_DB.DB_persontitle")
public class PersonTitle extends Title<Person> {
    public PersonTitle() {
    }

    public PersonTitle(Language language, String title, Person person) {
        super(language, title, person);
    }
}

