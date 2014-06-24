package md.varoinform.model.dao;

import md.varoinform.controller.Cache;
import md.varoinform.controller.comparators.EnterpriseComparator;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.entities.Tag;
import md.varoinform.model.util.SessionManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnterpriseDao extends GenericDaoHibernateImpl<Enterprise, Long>{
    public EnterpriseDao() {
        super(Enterprise.class);
    }

    public static List<Enterprise> getEnterprises(){
        //noinspection unchecked
       List<Enterprise> enterprises = SessionManager.getSession().createCriteria(Enterprise.class).list();
       Collections.sort(enterprises, new EnterpriseComparator());
       return enterprises;
    }

    public List<Enterprise> read(List<Long> ids) {
        if (Cache.instance.isEnterpriseCached()){
            return Cache.instance.getEnterprises(ids);
        } else {
            Criteria criteria = SessionManager.getSession().createCriteria(Enterprise.class).add(Restrictions.in("id", ids));
            //noinspection unchecked
            return criteria.list();
        }
    }

    public List<Long> getEnterpriseIdsByTag(Tag tag) {
        ArrayList<Long> ids = new ArrayList<>();
        for (Enterprise enterprise : tag.getEnterprises()) {
            ids.add(enterprise.getId());
        }
        return ids;
    }
}