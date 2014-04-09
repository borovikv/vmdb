package md.varoinform.controller.entityproxy;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.entities.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/13/13
 * Time: 11:04 AM
 */
public class ContactPersonProxy {
    private final ContactPerson contactPerson;
    private final List<Phone> phones;
    private Position position;
    private Person person;

    public ContactPersonProxy(ContactPerson contactPerson) {
        this.contactPerson = contactPerson;
        phones = contactPerson.getPhones();
        position = contactPerson.getPosition();
        person = contactPerson.getPerson();
    }

    public String getPersonTable() {
        if (contactPerson == null) return "";

        Language currentLanguage = LanguageProxy.instance.getCurrentLanguage();

        String title = "";
        if (person != null){
            title = person.title(currentLanguage);
        }
        String sPhones = phones.toString();
        return position.title(currentLanguage) + " " + title + ": " + sPhones.substring(1, sPhones.length()-1);
    }
}
