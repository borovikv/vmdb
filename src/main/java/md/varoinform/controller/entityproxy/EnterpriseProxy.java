package md.varoinform.controller.entityproxy;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.entities.GoodEnterprise;
import md.varoinform.entities.GoodType;
import md.varoinform.model.dao.ProductTypeDAO;
import md.varoinform.model.entities.address.AddressNode;
import md.varoinform.model.entities.address.PostalCode;
import md.varoinform.model.entities.base.Brand;
import md.varoinform.model.entities.enterprise.*;
import md.varoinform.model.entities.product.ProductType;
import md.varoinform.model.utils.DefaultClosableSession;

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
    private final List<Map<String, String>> contactProxy;

    public EnterpriseProxy(Enterprise enterprise, Integer langID) {
        super(langID);
        this.enterprise = enterprise;
        contactProxy = getContactProxy();
    }

    public EnterpriseProxy(Enterprise enterprise) {
        this(enterprise, LanguageProxy.instance.getCurrentLanguage());
    }

    private List<Map<String, String>> getContactProxy() {
        List<Map<String, String>> result = new ArrayList<>();
        List<Contact> contacts = enterprise.getContacts();
        for (Contact contact : contacts) {
            result.add(new ContactProxy(contact, getLangID()).getContactMap());
        }
        return result;
    }

    @Property(name = "Title")
    public String getTitle(){
        List<EnterpriseTitle> titles = enterprise.getTitles();
        String result = "";
        String delimiter = "";
        for (EnterpriseTitle title : titles) {
            String language = LanguageProxy.instance.getTitle(getLangID());
            String s = title.titleForLang(language);
            result += delimiter + s;
            delimiter = "/ ";
        }
        return result;
    }

    @Property(name = "IDNO")
    public String getIDNO() {
        return enterprise.getIdno();
    }

    @Property(name = "BusinessEntityType")
    public String getBusinessEntityType(){
        return getTitle(enterprise.getEnterpriseType());
    }

    @Property(name = "CreationDate")
    public Short getCreationDate(){
        return enterprise.getCreationYear();
    }

    @Property(name = "Workplaces")
    public Short getWorkplaces(){
        return enterprise.getNumberOfJobs();
    }

    @Property(name = "CheckDate")
    public Date getLastChange(){
        return enterprise.getLastChange();
    }


    @Property(name = "ContactPerson")
    public Map<String, Object> getContactPerson(){
        List<md.varoinform.model.entities.enterprise.Person> persons = enterprise.getPersons();
        Person contactPerson = persons.size() > 0 ? persons.get(0) : null;
        ContactPersonProxy proxy = new ContactPersonProxy(contactPerson, getLangID());
        return proxy.getPersonMap();
    }

    @Property(name = "PersonPhones")
    public String getContactPersonPhones(){
        String phones = (String) getContactPerson().get("personPhones");
        if (phones == null){
            return "";
        }
        return  phones;
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

        for (EnterpriseProduct product : enterprise.getProducts()){
            List<ProductType> productTypes = product.getProductType();
            for (ProductType productType : productTypes) {
                String t = productType.getTitle();
                Set<String> goodSet = goods.get(t);
                if (goodSet == null){
                    goodSet = new TreeSet<>();
                    goods.put(t, goodSet);
                }
                goodSet.add(getTitle(product.getProduct()));
            }
        }
        return goods;

    }

    @Property(name = "GoodTypes")
    public Map<Integer, String> getGoodTypes(){
        return ProductTypeDAO.getTypes();
    }

    @Property(name = "PostalCode")
    public Short getPostalCode(){
        List<Location> locations = enterprise.getLocations();
        for (Location location : locations) {
            PostalCode code = location.getAddressNode().getCode();
            if (code != null)
                return code.getCode();
            return null;
        }
        return null;
    }

    @Property(name = "office")
    public String getOfficeNumber() {
        List<Location> locations = enterprise.getLocations();
        for (Location location : locations) {
            return location.getOffice();
        }
        return null;
    }

    @Property(name = "house")
    public String getHouseNumber() {
        List<Location> locations = enterprise.getLocations();
        for (Location location : locations) {
            AddressNode addressNode = location.getAddressNode();
            if (addressNode.getType().getTitle().equalsIgnoreCase("number")){
                return addressNode.toString();
            }
        }
        return null;
    }

    @Property(name = "street")
    public String getStreet() {
        List<Location> locations = enterprise.getLocations();
        for (Location location : locations) {
            AddressNode addressNode = location.getAddressNode();
            if (addressNode.getType().getTitle().equalsIgnoreCase("number")){
                return addressNode.getParent().toString();
            } else if (addressNode.getType().getTitle().equalsIgnoreCase("street")){
                return addressNode.toString();
            }
        }
        return null;
    }

    @Property(name = "Town")
    public String getTown(){
        List<Location> locations = enterprise.getLocations();
        for (Location location : locations) {
            AddressNode addressNode = location.getAddressNode();
            if (addressNode.getType().getTitle().equalsIgnoreCase("number")){
                return addressNode.getParent().getParent().toString();
            } else if (addressNode.getType().getTitle().equalsIgnoreCase("street")){
                return addressNode.getParent().toString();
            } else if (addressNode.getType().getTitle().equalsIgnoreCase("street")){
                return addressNode.toString();
            }
        }
        return null;

    }

    @Property(name = "Emails")
    public List<String> getEmails(){
        List<String> result = new ArrayList<>();

        List<Email> emails = enterprise.getEmails();
        for (Email email : emails) {
            result.add(email.getEmail());
        }
        return result;
    }

    @Property(name = "Phones")
    public List<String> getPhones(){
        List<String> result = new ArrayList<>();

        for (Map<String, String> map : contactProxy) {
            result.add(map.get("phone"));
        }
        return result;
    }

    @Property(name = "Urls")
    public List<String> getUrls(){
        List<String> result = new ArrayList<>();

        List<WWW> urls = enterprise.getUrls();
        for (WWW url : urls) {
            result.add(url.getUrl());
        }
        return result;
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
