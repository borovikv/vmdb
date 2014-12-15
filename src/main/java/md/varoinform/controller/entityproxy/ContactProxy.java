package md.varoinform.controller.entityproxy;

import md.varoinform.model.entities.*;
import md.varoinform.util.ResourceBundleHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    public ContactProxy(Contact contact, Long langID) {
        super(langID);
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
        return ResourceBundleHelper.getString(getLangID(), "moldova", "Republic Moldova");
    }

    public List<String> getEmail(){
        return getStrings("getEmails");
    }

    public ArrayList<String> getStrings(String name) {
        ArrayList<String> strings = new ArrayList<>();
        if (contact != null) {
            try {
                Method method = Contact.class.getMethod(name);
                List<?> objs = (List<?>) method.invoke(contact);
                for (Object obj : objs) {
                    strings.add(obj.toString());
                }
            } catch (InvocationTargetException|NoSuchMethodException|IllegalAccessException ignored){

            }
        }
        return strings;
    }

    public List<String> getPhones(){
        return getStrings("getPhones");
    }

    public List<String> getUrls(){
        return getStrings("getUrls");
    }

    public List<String> getFax() {
        return getStrings("getFax");
    }

    public List<String> getGSM(){
        return getStrings("getGSM");
    }
}
