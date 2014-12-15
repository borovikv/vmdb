package md.varoinform.controller.entityproxy;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.entities.*;
import md.varoinform.util.ResourceBundleHelper;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@SuppressWarnings("UnusedDeclaration")
public class EnterpriseProxy extends EntityProxy {
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

    private final Enterprise enterprise;
    private final ContactProxy contactProxy;

    public EnterpriseProxy(Enterprise enterprise, Long langID) {
        super(langID);
        this.enterprise = enterprise;
        contactProxy = getContactProxy();
    }

    public EnterpriseProxy(Enterprise enterprise) {
        this(enterprise, LanguageProxy.instance.getCurrentLanguage());
    }

    private ContactProxy getContactProxy() {
        List<Contact> contacts = enterprise.getContacts();
        if (contacts.isEmpty()) return new ContactProxy(null, getLangID());
        return new ContactProxy(contacts.get(0), getLangID());
    }

    @Property(name = "Title")
    public String getTitle(){
        return getTitle(enterprise);
    }

    @Property(name = "IDNO")
    public String getIDNO() {
        return enterprise.getIdno();
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
        return ResourceBundleHelper.getString(getLangID(), String.valueOf(foreingCapital), String.valueOf(foreingCapital));
    }

    @Property(name = "Workplaces")
    public Integer getWorkplaces(){
        return enterprise.getWorkplaces();
    }

    public String getLogo(){
        return enterprise.getLogo();
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
        ContactPersonProxy proxy = new ContactPersonProxy(contactPerson, getLangID());
        return proxy.getPersonMap();
    }

    @Property(name = "PersonPhones")
    public Collection<String> getContactPersonPhones(){
        Object phones = getContactPerson().get("personPhones");
        if (phones == null){
            return new ArrayList<>();
        }
        //noinspection unchecked
        return (Collection<String>) phones;
    }

    @Property(name = "Brands")
    public Collection<String> getBrands(){
        List<String> brands = new ArrayList<>();
        for (Brand brand : enterprise.getBrands()) {
            brands.add(brand.toString());
        }
        return brands;
    }

    @Property(name = "Goods")
    public Map<String, Set<String>> getGoods(){
        Map<String, Set<String>> goods = new HashMap<>();
        for (GoodEnterprise g : enterprise.getGoods()) {
            GoodType type = g.getGoodType();
            String t = getTitle(type);
            Set<String> goodSet = goods.get(t);
            if (goodSet == null){
                goodSet = new TreeSet<>();
                goods.put(t, goodSet);
            }
            goodSet.add(getTitle(g.getGood()));
        }
        return goods;

    }

    @Property(name = "GoodTypes")
    public Map<Long, String> getGoodTypes(){
        Map<Long, String> types = new HashMap<>();
        for (GoodEnterprise g : enterprise.getGoods()) {
            GoodType type = g.getGoodType();
            Long typeId = type.getId();
            types.put(typeId, getTitle(type));
        }
        return types;
    }

    @Property(name = "PostalCode")
    public String getPostalCode(){
        return contactProxy.getPostalCode();
    }

    @Property(name = "office")
    public String getOfficeNumber() {
        return contactProxy.getOfficeNumber();
    }

    @Property(name = "house")
    public String getHouseNumber() {
        return contactProxy.getHouseNumber();
    }

    @Property(name = "street")
    public String getStreet() {
        return contactProxy.getStreet();
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

    //@Property(name = "Country")
    public String getCountry(){
        return contactProxy.getCountry();
    }

    @Property(name = "Emails")
    public List<String> getEmails(){

        return contactProxy.getEmail();
    }

    @Property(name = "Phones")
    public List<String> getPhones(){

        return contactProxy.getPhones();
    }

    @Property(name = "Urls")
    public List<String> getUrls(){

        return contactProxy.getUrls();
    }

    @Property(name = "Faxes")
    public List<String> getFaxes() {
        return contactProxy.getFax();
    }

    @Property(name = "GSM")
    public List<String> getGSM(){
        return contactProxy.getGSM();
    }


    public Object get(String name){
        try {
            String key = name.toLowerCase();
            Method method = methods.get(key);
            if (method != null) {
                return method.invoke(this);
            }
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
        return Arrays.asList("country", "postalcode", "sector", "region", "town", "street", "house", "office");
    }
}
