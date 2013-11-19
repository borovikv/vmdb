package md.varoinform.controller.entityproxy;

import md.varoinform.model.entities.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class EnterpriseProxy extends EntityProxy {
    private final Enterprise enterprise;
    private static Map<String, Method> methods;
    static {
        EnterpriseProxy.methods = new HashMap<>();
        Method[]methods = EnterpriseProxy.class.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Property.class)) {
                Property property = method.getAnnotation(Property.class);
                EnterpriseProxy.methods.put(property.name().toLowerCase(), method);
            }
        }
    }

    public EnterpriseProxy(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    @Property(name = "Title")
    public String getTitle(){
        return getTitle(enterprise);
    }

    @Property(name = "BusinessEntityType")
    public String getBusinessEntityType(){
        return getTitle(enterprise.getBusinessEntityType());
    }

    @Property(name = "CreationDate")
    public String getCreationDate(){
        return getStringValueOrEmpty(enterprise.getCreation());
    }

    @Property(name = "ForeingCapital")
    public String getForeingCapital(){
        Boolean foreingCapital = enterprise.getForeingCapital();
        return foreingCapital != null ? getResourceBundle().getString(foreingCapital.toString()) : "";
    }

    @Property(name = "Workplaces")
    public String getWorkplaces(){
        return getStringValueOrEmpty(enterprise.getWorkplaces());
    }

    public String getLogo(){
        return enterprise.getLogo();
    }

    @Property(name = "YPUrl")
    public String getYPUrl(){
        return enterprise.getYpUrl();
    }

    @Property(name = "CheckDate")
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

    @Property(name = "ContactPerson")
    public String getContactPerson(){
        StringBuilder result = new StringBuilder();
        for (ContactPerson contactPerson : enterprise.getContactPersons()) {
            ContactPersonProxy proxy = new ContactPersonProxy(contactPerson);
            result.append(proxy.getPersonTable());
        }

        return result.toString();
    }

    @Property(name = "Brands")
    public String getBrands(){
        StringBuilder result = new StringBuilder();
        for (Brand brand : enterprise.getBrands()) {
            result.append(brand.getTitle());
            result.append(", ");
        }
        return result.toString();
    }

    @Property(name = "Goods")
    public String getGoods(){
        StringBuilder result = new StringBuilder();
        for (GProduce gProduce : enterprise.getGoods()) {
            String isProduced = getResourceBundle().getString("" + gProduce.getProduce());
            result.append(getTitle(gProduce.getGood()) + "( " + isProduced + " ); ");
        }
        return result.toString();
    }

    @Property(name = "Branches")
    public String getBranches(){
        StringBuilder result = new StringBuilder();
        for (Branch branch : enterprise.branches()) {
            result.append(getTitle(branch));
            result.append("; ");
        }
        return result.toString();
    }


    public ContactProxy getContactProxy(){
        return getContactProxies().get(0);
    }

    @Property(name = "Address")
    public String getAddress() {
        return  getContactProxy().getAddress();
    }

    @Property(name = "PostalCode")
    public String getPostalCode(){
        return getContactProxy().getPostalCode();
    }

    //@Property(name = "HouseNumber")
    public String getHouseNumber(){

        return getContactProxy().getHouseNumber();
    }

    //@Property(name = "OfficeNumber")
    public String getOfficeNumber(){
        return getContactProxy().getOfficeNumber();
    }

    //@Property(name = "Street")
    public String getStreet(){
        return getContactProxy().getStreet();
    }

    //@Property(name = "Sector")
    public String getSector(){
        return getContactProxy().getSector();
    }

    //@Property(name = "Town")
    public String getTown(){
        return getContactProxy().getTown();
    }

    //@Property(name = "Region")
    public String getRegion(){
        return getContactProxy().getRegion();
    }

    //@Property(name = "TopAdministrativeUnit")
    public String getTopAdministrativeUnit(){
        return getContactProxy().getTopAdministrativeUnit();
    }

    //@Property(name = "Country")
    public String getCountry(){
        return getContactProxy().getCountry();
    }

    @Property(name = "Emails")
    public String getEmails(){
        return getContactProxy().getEmail();
    }

    @Property(name = "Phones")
    public String getPhones(){
        return getContactProxy().getPhones();
    }

    @Property(name = "Urls")
    public String getUrls(){
        return getContactProxy().getUrls();
    }

    @Property(name = "Faxes")
    public String getFaxes() {
        return getContactProxy().getFax();
    }


    public String get(String name){
        try {
            return (String)methods.get(name.toLowerCase()).invoke(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<String> getViewPartNames(){
        return new ArrayList<>(methods.keySet());
    }




}
