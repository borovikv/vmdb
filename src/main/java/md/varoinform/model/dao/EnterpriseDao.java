package md.varoinform.model.dao;

import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.util.SessionManager;
import org.hibernate.Query;
import java.util.List;

public class EnterpriseDao {
    public EnterpriseDao() {
    }

    public static List<Enterprise> getEnterprisesByBranchId(List<Long> branchIds) {
        String hql = "Select distinct " +
                "e from Enterprise e join e.goods good " +
                "where good.good.treeNode.id in(:branchIds) ";

        Query query = SessionManager.getSession().createQuery(hql).setParameterList("branchIds", branchIds);

        @SuppressWarnings("unchecked")
        List<Enterprise> list = query.list();
        return list;
    }
}