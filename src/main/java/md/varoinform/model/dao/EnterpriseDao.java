package md.varoinform.model.dao;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.entities.Language;
import md.varoinform.model.util.SessionManager;
import org.hibernate.Query;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class EnterpriseDao {
    public EnterpriseDao() {
    }

    public static List<Enterprise> getEnterprisesByBranchId(List<Long> branchIds) {
        String hql = "Select distinct " +
                "e from Enterprise e " +
                "join e.goods good " +
                "join good.good.nodes tn  " +
                "where tn.id in(:branchIds)";

        //noinspection unchecked
        List<Enterprise> enterprises = executeQuery(hql, "branchIds", branchIds);
        sortEnterprises(enterprises);
        return enterprises;
    }

    private static void sortEnterprises(List<Enterprise> enterprises) {
        Collections.sort(enterprises, new Comparator<Enterprise>() {
            @Override
            public int compare(Enterprise o1, Enterprise o2) {
                Language lang = LanguageProxy.instance.getCurrentLanguage();
                Locale locale = new Locale(LanguageProxy.getCurrentLanguageTitle());
                Collator collator = Collator.getInstance(locale);
                String titleO1 = o1.title(lang);
                String titleO2 = o2.title(lang);
                return collator.compare(titleO1, titleO2);
            }
        });
    }

    private static List  executeQuery(String hql, String property, List branchIds) {
        Query query = SessionManager.getSession().createQuery(hql).setParameterList(property, branchIds);

        return query.list();
    }

    public static List<Enterprise> getEnterprises(){
        //noinspection unchecked
        return SessionManager.getSession().createCriteria(Enterprise.class).list();
    }
}