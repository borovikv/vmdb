package md.varoinform.controller.entityproxy;

import md.varoinform.model.dao.EnterpriseDao;
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


    public EnterpriseProxy(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    public EnterpriseProxy(Enterprise enterprise, Language language) {
        super(language);
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
        Boolean foreingCapital = enterprise.getForeignCapital();
        return foreingCapital != null ? ResourceBundleHelper.getString(foreingCapital.toString()) : "";
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
    public Map<String, Object> getContactPerson(){
        List<ContactPerson> contactPersons = enterprise.getContactPersons();
        ContactPerson contactPerson = contactPersons.size() > 0 ? contactPersons.get(0) : null;
        ContactPersonProxy proxy = new ContactPersonProxy(contactPerson);
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

    @Property(name = "Branches")
    public String getBranches(){
        Set<String> titleNodes = new TreeSet<>();
        for (TreeNode treeNode : EnterpriseDao.getNodeByEnterprise(enterprise)) {
            titleNodes.add(getTitle(treeNode.getTitle()));
        }
        return getString(titleNodes);
    }

    private String getString(Collection<String> collection) {
        String result = String.valueOf(collection);
        return result.substring(1, result.length() - 1);
    }


    public ContactProxy getContactProxy(){
        return getContactProxies().get(0);
    }

    @Property(name = "Address")
    public List<String> getAddress() {
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

    //@Property(name = "Country")
    public String getCountry(){
        return getContactProxy().getCountry();
    }

    @Property(name = "Emails")
    public List<Email> getEmails(){
        return getContactProxy().getEmail();
    }

    @Property(name = "Phones")
    public List<Phone> getPhones(){
        return getContactProxy().getPhones();
    }

    @Property(name = "Urls")
    public List<Url> getUrls(){
        return getContactProxy().getUrls();
    }

    @Property(name = "Faxes")
    public List<Phone> getFaxes() {
        return getContactProxy().getFax();
    }


    public Object get(String name){
        try {
            String key = name.toLowerCase();
            return methods.get(key).invoke(this);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<String> getFields(){
        return new ArrayList<>(methods.keySet());
    }




}
