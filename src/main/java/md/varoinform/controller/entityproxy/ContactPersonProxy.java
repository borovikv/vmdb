package md.varoinform.controller.entityproxy;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.entities.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/13/13
 * Time: 11:04 AM
 */
public class ContactPersonProxy {
    private final ContactPerson contactPerson;

    public ContactPersonProxy(ContactPerson contactPerson) {
        this.contactPerson = contactPerson;
    }

    public Map<String, Object> getPersonMap() {
        Map<String, Object> map = new HashMap<>();
        if (contactPerson == null || contactPerson.getPerson() == null) return map;

        Language currentLanguage = LanguageProxy.instance.getCurrentLanguage();

        map.put("personTitle", contactPerson.getPerson().title(currentLanguage));
        map.put("personPosition", contactPerson.getPosition().title(currentLanguage));
        if (contactPerson.getPhones().size() > 0)
            map.put("personPhones", contactPerson.getPhones());

        return map;
    }
}
