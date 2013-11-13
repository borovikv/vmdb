package md.varoinform.controller;

import md.varoinform.model.entities.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/13/13
 * Time: 10:46 AM
 */
public class EnterpriseProxy extends EntityProxy {
    private final Enterprise enterprise;

    public EnterpriseProxy(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    public String getTitle(){
        return getTitle(enterprise);
    }

    public String getBusinessEntityType(){
        return getTitle(enterprise.getBusinessEntityType());
    }

    public String getCreationDate(){
        return getStringValueOrEmpty(enterprise.getCreation());
    }

    public String getForeingCapital(){
        Boolean foreingCapital = enterprise.getForeingCapital();
        return foreingCapital != null ? getResourceBundle().getString(foreingCapital.toString()) : "";
    }

    public String getWorkplaces(){
        return getStringValueOrEmpty(enterprise.getWorkplaces());
    }

    public String getLogo(){
        return enterprise.getLogo();
    }

    public String getYPUrl(){
        return enterprise.getYpUrl();
    }

    public String getCheckDate(){
        return getStringValueOrEmpty(enterprise.getCheckDate());
    }

    public String getLastChange(){
        return getStringValueOrEmpty(enterprise.getLastChange());
    }

    public List<ContactProxy> getContactProxies(){
        List<ContactProxy> result = new ArrayList<>();
        for (Contact contact : enterprise.getContacts()) {
            result.add(new ContactProxy(contact));
        }
        return result;
    }

    public String getAdvertisement(){
        StringBuilder result = new StringBuilder();
        for (Advertisement advertisement : enterprise.getAdvertisements()) {
            AdvertisementProxy proxy = new AdvertisementProxy(advertisement);
            result.append(proxy.getAdvertisementTable());

        }
        return result.toString();
    }

    public String getContactPerson(){
        StringBuilder result = new StringBuilder();
        for (ContactPerson contactPerson : enterprise.getContactPersons()) {
            ContactPersonProxy proxy = new ContactPersonProxy(contactPerson);
            result.append(proxy.getPersonTable());
        }

        return result.toString();
    }

    public String getBrands(){
        StringBuilder result = new StringBuilder();
        for (Brand brand : enterprise.getBrands()) {
            result.append(brand.getTitle());
            result.append(", ");
        }
        return result.toString();
    }
    public String getGoods(){
        StringBuilder result = new StringBuilder();
        for (GProduce gProduce : enterprise.getGoods()) {
            String isProduced = getResourceBundle().getString("" + gProduce.getProduce());
            result.append(getTitle(gProduce.getGood()) + "( " + isProduced + " ); ");
        }
        return result.toString();
    }
    public String getBranches(){
        StringBuilder result = new StringBuilder();
        for (Branch branch : enterprise.branches()) {
            result.append(getTitle(branch));
            result.append("; ");
        }
        return result.toString();
    }


}
