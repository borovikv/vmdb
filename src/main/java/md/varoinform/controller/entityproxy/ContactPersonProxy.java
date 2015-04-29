package md.varoinform.controller.entityproxy;

import md.varoinform.model.entities.enterprise.Person;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/13/13
 * Time: 11:04 AM
 */
public class ContactPersonProxy extends EntityProxy{
    private final Person contactPerson;

    public ContactPersonProxy(Person contactPerson, Integer langID) {
        super(langID);
        this.contactPerson = contactPerson;
    }

    public Map<String, Object> getPersonMap() {
        Map<String, Object> map = new HashMap<>();
        if (contactPerson == null) return map;

        map.put("personTitle", getTitle(contactPerson.getFirstName()) + " " + getTitle(contactPerson.getLastName()));
        map.put("personPosition", getTitle(contactPerson.getPosition()));
        if (contactPerson.getPhone() != null)
            map.put("personPhones", contactPerson.getPhone().toString());

        return map;
    }
}
