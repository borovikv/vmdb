package md.varoinform.modeltest.util;

import md.varoinform.model.dao.TransactionDaoHibernateImpl;
import md.varoinform.model.entities.*;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/10/13
 * Time: 9:45 AM
 */
public class EntityCreator {
    private EntityCreator() {
    }


    public List<Branch> createBranches(){
        String [] rusBranchTitle = {"регклама", "дизайн", "печать", "мебель и деревообрабатывающая промышленность", "Дом и быт"};
        String [] engBranchTitle = {"Advertisement", "design", "printing", "furniture and wood", "House and family life"};
        List<Branch> branches = new ArrayList<>();
        for (int i = 0; i < rusBranchTitle.length && i < engBranchTitle.length; i++) {
            Branch b = new Branch();
            save(Branch.class, b);
            BranchTitle btr = new BranchTitle(getLanguage("rus"), rusBranchTitle[i], b);
            BranchTitle bte = new BranchTitle(getLanguage("eng"), engBranchTitle[i], b);
            save(BranchTitle.class, btr);
            save(BranchTitle.class, bte);
            branches.add(b);
        }
        return branches;
    }

    public List<Brand> createBrands(){
        String[] titles = {"Varo", "techno-design", "Adobe", "Ariel",  "Woodland"};
        List<Brand> brands = new ArrayList<>();
        for (String title : titles) {
            Brand b = new Brand(title);
            save(Brand.class, b);
            brands.add(b);
        }
        return brands;
    }



    public void createContact(Enterprise enterprise, Town town, Street street, String house, String[] phones){
        List<Phone> phoneList = createPhones(phones);
        Contact c = new Contact();
        c.setEnterprise(enterprise);
        c.setPhones(phoneList);
        c.setTown(town);
        c.setStreet(street);
        c.setHouseNumber(house);
        save(Contact.class, c);
    }

    public List<Street> createStreets(){
        String[] rusTitles = {"Г.Тудор", "Шт.чел Маре", "Еминеску"};
        String[] engTitles = {"G.Tudor", "St.cel Mare", "Eminescu"};
        List<Street> result = new ArrayList<>();
        for (int i = 0; i < rusTitles.length && i < engTitles.length; i++) {
            Street street = new Street();
            save(Street.class, street);
            StreetTitle str = new StreetTitle(getLanguage("rus"), rusTitles[i], street);
            StreetTitle ste = new StreetTitle(getLanguage("eng"), engTitles[i], street);
            save(StreetTitle.class, str);
            save(StreetTitle.class, ste);
            result.add(street);
        }
        return result;
    }
    public List<Town> createTowns(){
        String[] rusTitles = {"кишинев"};
        String[] engTitles = {"chisinau"};
        List<Town> result = new ArrayList<>();
        for (int i = 0; i < rusTitles.length && i < engTitles.length; i++) {
            Town town = new Town();
            save(Town.class, town);
            TownTitle ttr = new TownTitle(getLanguage("rus"), rusTitles[i], town);
            TownTitle tte = new TownTitle(getLanguage("eng"), engTitles[i], town);
            save(TownTitle.class, ttr);
            save(TownTitle.class, tte);
            result.add(town);
        }
        return result;

    }
    public List<Phone> createPhones(String... phones){
        List<Phone> result = new ArrayList<>();
        for (String phone : phones) {
            Phone p = new Phone(phone);
            save(Phone.class, p);
            result.add(p);
        }
        return result;
    }

    public List<Good> createGood(){
        String[] rusTitles = {"плакаты", "макеты", "печать", "гвозди", "утюги"};
        String[] engTitles = {"posters", "models", "print", "nails", "irons"};
        // "регклама", "дизайн", "печать", "мебель и деревообрабатывающая промышленность", "Дом и быт"
        List<Branch> branches = createBranches();
        List<Good> result = new ArrayList<>();

        for (int i = 0; i < rusTitles.length && i < engTitles.length; i++) {
            Good g = new Good();
            int branchIndex = i % Math.min(rusTitles.length, engTitles.length);
            g.setBranch(branches.get(branchIndex));
            save(Good.class, g);
            GoodTitle gtr = new GoodTitle(getLanguage("rus"), rusTitles[i], g);
            GoodTitle gte = new GoodTitle(getLanguage("eng"), engTitles[i], g);
            save(GoodTitle.class, gtr);
            save(GoodTitle.class, gte);
            result.add(g);
        }
        return result;
    }

    public Enterprise createEnterprise(List<Brand> brands, List<Good> goods, String... titles){
        Enterprise e = new Enterprise();
        e.setBrands(brands);
        save(Enterprise.class, e);
        for (Good good : goods) {
            GProduce gProduce = new GProduce(e, good, Boolean.FALSE);
            save(GProduce.class, gProduce);
        }

        for (String title : titles) {
            EnterpriseTitle et = new EnterpriseTitle(getLanguage("rus"), title, e);
            save(EnterpriseTitle.class, et);
        }
        return e;
    }

    public List<Enterprise> createEnterprises(){
        // кишинев
        List<Town> towns = createTowns();

        //"Г.Тудор", "Шт.чел Маре", "Еминеску"
        List<Street> streets = createStreets();

        // "Varo", "techno-design", "Adobe", "Ariel",  "Woodland"
        List<Brand> allBrands = createBrands();

        //"плакаты", "макеты", "печать", "гвозди", "утюги"
        List<Good> allGoods = createGood();

        List<Enterprise> result = new ArrayList<>();

        Enterprise e1 = createEnterprise(allBrands.subList(0,1), allGoods.subList(0,3), "Varo");
        String[] phones1 = {"111111", "111222"};
        createContact(e1, towns.get(0), streets.get(0), "1", phones1);
        createContact(e1, towns.get(0), streets.get(1), "2", phones1);
        result.add(e1);

        Enterprise e2 = createEnterprise(allBrands.subList(1,3), allGoods.subList(1,2), "Polygraph");
        String[] phones2 = {"222111"};
        createContact(e2, towns.get(0), streets.get(0), "1", phones2);
        result.add(e2);

        Enterprise e3 = createEnterprise(allBrands.subList(3, 4), allGoods.subList(3,4), "house&Co");
        String[] phones3 = {"333111", "333222"};
        createContact(e3, towns.get(0), streets.get(2), "1", phones3);
        result.add(e3);

        return result;
    }


    private Language getLanguage(String title) {
        Language l = get(Language.class, "title", title);
        if (l == null){
            l = new Language(title);
            save(Language.class, l);
        }
        return l;
    }


    private static <T> void save(Class<T> type, T inst) {
        (new TransactionDaoHibernateImpl<T, Long>(type)).create(inst);
    }

    private static <T> T get(Class<T> type, String property, Object value){
        TransactionDaoHibernateImpl<T, Long> dao = new TransactionDaoHibernateImpl<T, Long>(type);
        Session session = dao.getSession();
        List<T> list = session.createCriteria(type).add(Restrictions.eq(property, value)).list();
        if (!list.isEmpty()){
            return list.get(0);
        }
        return null;
    }

}
