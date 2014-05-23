package md.varoinform.controller.entityproxy;

import md.varoinform.model.entities.*;
import md.varoinform.util.ResourceBundleHelper;

import java.util.*;

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
    private Contact contact;

    public ContactProxy(Contact contact, Language language) {
        super(language);
        this.contact = contact;
    }

    public String getPostalCode(){
        if (contact == null) return null;
        String postalCode = contact.getPostalCode();
        return postalCode.toUpperCase().startsWith("MD") ? postalCode : "MD-" + postalCode;
    }

    public String getHouseNumber(){

        return contact == null ? null : contact.getHouseNumber();
    }

    public String getOfficeNumber(){
        return contact == null ? null : contact.getOfficeNumber();
    }

    public String getStreet(){
        return contact == null ? null : getTitle(contact.getStreet());
    }

    public String getSector(){
        return contact == null ? null : getTitle(contact.getSector());
    }

    public String getTown(){
        return contact == null ? null : getTitle(contact.getTown());
    }

    public String getRegion(){
        return contact == null ? null : getTitle(contact.getRegion());
    }



    public String getCountry(){
        return ResourceBundleHelper.getString(currentLanguage(), "moldova", "Republic Moldova");
    }

    public List<Email> getEmail(){
        return contact == null ? new ArrayList<Email>() : contact.getEmails();
    }

    public List<Phone> getPhones(){
        return contact == null ? new ArrayList<Phone>() : contact.getPhones();
    }

    public List<Url> getUrls(){
        return contact == null ? new ArrayList<Url>() : contact.getUrls();
    }

    public List<Phone> getFax() {
       return contact == null ? new ArrayList<Phone>() : contact.getFax();
    }
}
