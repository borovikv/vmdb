package md.varoinform.controller.entityproxy;

import md.varoinform.model.entities.*;
import md.varoinform.util.ResourceBundleHelper;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@SuppressWarnings("UnusedDeclaration")
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

    private final ContactProxy contactProxy;


    public EnterpriseProxy(Enterprise enterprise) {
        this.enterprise = enterprise;
        contactProxy = getContactProxy();
    }

    private ContactProxy getContactProxy() {
        List<Contact> contacts = enterprise.getContacts();
        if (contacts.isEmpty()) return new ContactProxy(null, currentLanguage());
        return new ContactProxy(contacts.get(0), currentLanguage());
    }

    public EnterpriseProxy(Enterprise enterprise, Language language) {
        super(language);
        this.enterprise = enterprise;
        contactProxy = getContactProxy();
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
    public Integer getCreationDate(){
        return enterprise.getCreation();
    }

    @Property(name = "ForeingCapital")
    public String getForeingCapital(){
        Boolean foreingCapital = enterprise.getForeignCapital();
        return ResourceBundleHelper.getString(currentLanguage(), String.valueOf(foreingCapital), String.valueOf(foreingCapital));
    }

    @Property(name = "Workplaces")
    public Integer getWorkplaces(){
        return enterprise.getWorkplaces();
    }

    public String getLogo(){
        return enterprise.getLogo();
    }

    @Property(name = "YPUrl")
    public String getYPUrl(){
        return enterprise.getYpUrl();
    }

    @Property(name = "CheckDate")
    public Date getCheckDate(){
        return enterprise.getCheckDate();
    }

    public Date getLastChange(){
        return enterprise.getLastChange();
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
    public Map<String, Object> getContactPerson(){
        List<ContactPerson> contactPersons = enterprise.getContactPersons();
        ContactPerson contactPerson = contactPersons.size() > 0 ? contactPersons.get(0) : null;
        ContactPersonProxy proxy = new ContactPersonProxy(contactPerson, currentLanguage());
        return proxy.getPersonMap();
    }

    @Property(name = "Brands")
    public Collection<Brand> getBrands(){
        return enterprise.getBrands();
    }

    @Property(name = "Goods")
    public Collection<String> getGoods(){
        Set<String> goods = new TreeSet<>();
        for (G2Produce gProduce : enterprise.getGoods()) {
            goods.add(getTitle(gProduce.getGood()));
        }
        return goods;

    }

    @Property(name = "PostalCode")
    public String getPostalCode(){
        return contactProxy.getPostalCode();
    }

    @Property(name = "StreetHouseOffice")
    public List<String> getStreetHouseOffice(){
        return Arrays.asList(contactProxy.getStreet(), contactProxy.getHouseNumber(), contactProxy.getOfficeNumber());
    }

    @Property(name = "Sector")
    public String getSector(){

        return contactProxy.getSector();
    }

    @Property(name = "Town")
    public String getTown(){

        return contactProxy.getTown();
    }

    @Property(name = "Region")
    public String getRegion(){
        return contactProxy.getRegion();
    }

    @Property(name = "Country")
    public String getCountry(){
        return contactProxy.getCountry();
    }

    @Property(name = "Emails")
    public List<Email> getEmails(){

        return contactProxy.getEmail();
    }

    @Property(name = "Phones")
    public List<Phone> getPhones(){

        return contactProxy.getPhones();
    }

    @Property(name = "Urls")
    public List<Url> getUrls(){

        return contactProxy.getUrls();
    }

    @Property(name = "Faxes")
    public List<Phone> getFaxes() {

        return contactProxy.getFax();
    }


    public Object get(String name){
        try {
            String key = name.toLowerCase();
            Method method = methods.get(key);
            if (method != null)
                return method.invoke(this);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Class getType(String name){
        String key = name.toLowerCase();
        if (Arrays.asList("workplaces", "creationdate", "checkdate").contains(key)){
            Method method = methods.get(key);
            return method.getReturnType();
        }
        return String.class;
    }

    public static List<String> getFields(){
        return new ArrayList<>(methods.keySet());
    }

    public static boolean isTitle(String field){
        return field.equalsIgnoreCase("title");
    }

    public static boolean isAddress(String field){
        return getAddressFields().contains(field.toLowerCase());
    }

    public static List<String> getAddressFields() {
        return Arrays.asList("country", "postalcode", "sector", "region", "town", "streethouseoffice");
    }
}
