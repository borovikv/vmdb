package md.varoinform.controller.entityproxy;

import md.varoinform.model.entities.*;

import java.util.List;

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

    public ContactProxy(Contact contact) {
        super();
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

    public String getTopAdministrativeUnit(){
        return getTitle(contact.getTopAdministrativeUnit());
    }


    public String getCountry(){
        return "Молдова";
    }

    public String getEmail(){
        StringBuilder result = new StringBuilder();
        String separator = "";
        for (Email email : contact.getEmails()) {
            result.append(separator);
            result.append(email.getEmail());
            separator = ", ";
        }
        return result.toString();
    }

    public String getPhones(){
        List<Phone> phones = contact.getPhones();
        return concatPhones(phones);
    }

    private String concatPhones(List<Phone> phones) {
        StringBuilder result = new StringBuilder();
        String separator = "";
        for (Phone phone : phones) {
            result.append(separator);
            result.append(phone.getPhone());
            separator = ", ";
        }
        return result.toString();
    }

    public String getUrls(){
        StringBuilder result = new StringBuilder();
        String separator = "";
        for (Url url : contact.getUrls()) {
            result.append(separator);
            result.append(url.getUrl());
            separator = ", ";
        }
        return result.toString();
    }

    public String getAddress() {
        String[] addressParts = { getCountry(), getTopAdministrativeUnit(), getRegion(), getTown(), getSector(),
                getStreet(), getHouseNumber(), getOfficeNumber()};

        StringBuilder result = new StringBuilder();
        String separator = "";
        for (String part: addressParts) {
            if (part.isEmpty()) continue;
            result.append(separator);
            result.append(part);
            separator = ", ";
        }

        return result.toString();
    }



    public String getFax() {
       return concatPhones(contact.getFax());
    }
}
