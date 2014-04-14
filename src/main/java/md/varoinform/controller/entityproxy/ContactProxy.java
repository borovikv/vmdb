package md.varoinform.controller.entityproxy;

import md.varoinform.model.entities.*;

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
        return getStringValueOrEmpty(contact.getPostalCode());
    }

    public String getHouseNumber(){

        return getStringValueOrEmpty(contact.getHouseNumber());
    }

    public String getOfficeNumber(){
        return getStringValueOrEmpty(contact.getOfficeNumber());
    }

    public String getStreet(){
        return getTitle(contact.getStreet());
    }

    public String getSector(){
        return getTitle(contact.getSector());
    }

    public String getTown(){
        return getTitle(contact.getTown());
    }

    public String getRegion(){
        return getTitle(contact.getRegion());
    }



    public String getCountry(){
        return null;
    }

    public List<Email> getEmail(){
        return contact.getEmails();
    }

    public List<Phone> getPhones(){
        return contact.getPhones();
    }

    public List<Url> getUrls(){
        return contact.getUrls();
    }

    public List<String> getAddress() {
        String country = getCountry();
        String sector = getSector();
        String region = getRegion();
        String town = getTown();
        String street = getStreet();
        String houseNumber = getHouseNumber();
        String officeNumber = getOfficeNumber();

        String [] parts;
        if (sector !=null && region != null &&sector.equals(region) && region.equals(town)) {
            parts = new String[] {country, sector, region, town, street, houseNumber, officeNumber};
        } else if (sector !=null && sector.equals(region)){
            parts = new String[] {country, region, town, street, houseNumber, officeNumber};
        } else {
            parts = new String[] {country, sector, region, town, street, houseNumber, officeNumber};
        }

        List<String> list = new ArrayList<>();
        list.addAll(addIfNotNull(parts));
        return list;

    }

    private List<String> addIfNotNull(String[] strings) {
        List<String> list = new ArrayList<>();
        for (String s : strings) {
            if (s == null || s.isEmpty()) continue;
            list.add(s);
        }
        return list;
    }


    public List<Phone> getFax() {
       return contact.getFax();
    }
}
