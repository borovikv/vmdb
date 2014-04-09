package md.varoinform.model.dao;

import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.entities.TreeNode;
import md.varoinform.model.util.SessionManager;
import org.hibernate.Query;
import org.hibernate.type.LongType;

import java.util.*;

public class EnterpriseDao {
    public EnterpriseDao() {
    }

    public static List<Enterprise> getEnterprisesByBranchId(List<Long> branchIds) {
        String hql = "Select distinct " +
                "e from Enterprise e " +
                "join e.goods good " +
                "join good.good.treeNodes tn " +
                "where tn.id in(:branchIds) ";

        //noinspection unchecked
        return executeQuery(hql, "branchIds", branchIds);
    }

    private static List  executeQuery(String hql, String property, List branchIds) {
        Query query = SessionManager.getSession().createQuery(hql).setParameterList(property, branchIds);

        return query.list();
    }

    public static List<TreeNode> getNodeByEnterprise(Enterprise e) {
        String sql = "SELECT distinct tn.id FROM EXPORTED_DB.DB_TREENODE tn  join EXPORTED_DB.DB_GOOD_TREE gt on tn.id = gt.NODE_ID " +
                "join EXPORTED_DB.DB_GOOD2 g2 on gt.good_id = g2.id " +
                "join EXPORTED_DB.DB_G2PRODUCE g2p on g2.id = g2p.good_id " +
                "where g2p.enterprise_id = " + e.getId();
        Query query = SessionManager.getSession().createSQLQuery(sql).addScalar("id", LongType.INSTANCE);
        String hql = "Select distinct tn from TreeNode tn where tn.id in (:ids)";

        //noinspection unchecked
        return executeQuery(hql, "ids", query.list());

    }
}