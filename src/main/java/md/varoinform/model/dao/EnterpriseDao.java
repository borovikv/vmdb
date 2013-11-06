package md.varoinform.model.dao;

import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.util.HibernateSessionFactory;
import org.hibernate.Query;
import java.util.List;

public class EnterpriseDao {
    public EnterpriseDao() {
    }

    public static List<Enterprise> getEnterprisesByBranchId(List<Long> branchIds) {
        String hql = "Select distinct " +
                "e from Enterprise e join e.goods good " +
                "where good.good.branch.id in(:branchIds) ";

        Query query = HibernateSessionFactory.getSession().createQuery(hql).setParameterList("branchIds", branchIds);
        return query.list();
    }
}