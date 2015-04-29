package md.varoinform.controller.entityproxy;

import md.varoinform.model.entities.enterprise.Contact;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/13/13
 * Time: 10:58 AM
 */
/*
    private String postalCode;
    private String houseNumber;
    private String officeNumber;
    private Street street;
    private Sector sector;
    private Town town;
    private Region region;
    private TopAdministrativeUnit topAdministrativeUnit;
    private List<Email> emails = new ArrayList<>();
    private List<Phone> phones = new ArrayList<>();
    private List<Url> urls = new ArrayList<>();
 */
public class ContactProxy extends EntityProxy {
    private Map<String, String> contactMap = new HashMap<>();

    public ContactProxy(Contact contact, Integer langID) {
        super(langID);
        contactMap.put("department", getTitle(contact.getDepartment()));
        contactMap.put("phone", contact.getPhone().toString());
        contactMap.put("position", getTitle(contact.getPosition()));
    }

    public Map<String, String> getContactMap() {
        return contactMap;
    }
}
