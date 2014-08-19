package md.varoinform.controller.entityproxy;

import md.varoinform.model.entities.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/13/13
 * Time: 11:04 AM
 */
public class ContactPersonProxy extends EntityProxy{
    private final ContactPerson contactPerson;

    public ContactPersonProxy(ContactPerson contactPerson, Long langID) {
        super(langID);
        this.contactPerson = contactPerson;
    }

    public Map<String, Object> getPersonMap() {
        Map<String, Object> map = new HashMap<>();
        if (contactPerson == null || contactPerson.getPerson() == null) return map;

        map.put("personTitle", getTitle(contactPerson.getPerson()));
        map.put("personPosition", getTitle(contactPerson.getPosition()));
        if (contactPerson.getPhones().size() > 0)
            map.put("personPhones", contactPerson.getPhones());

        return map;
    }
}
