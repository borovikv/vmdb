package md.varoinform.modeltest.util;

import md.varoinform.model.entities.TreeNode;
import md.varoinform.model.util.SessionManager;
import org.hibernate.Query;

import java.util.ArrayList;
import java.util.List;

public class BranchProxy {
    public BranchProxy() {
        
    }

    // Select treeNode, branch_title
    public static List<BranchProxyView> getBranchProxyView(Long language_id, Long parent_id) {

        String hql = "select b, bt.title from TreeNode b, BranchTitle bt " +
                "where " +
                "b.parent.id = " + parent_id +
                " and " +
                "b.id = bt.container " +
                "and " +
                "bt.language.id = :language_id " +
                "order by bt.title";

        Query query = SessionManager.getSession().createQuery(hql)
                .setParameter("language_id", language_id);

        @SuppressWarnings("unchecked")
        List<Object[]> result = query.list();
        List<BranchProxyView> branches = new ArrayList<>();
        for (Object[] o : result) {
            branches.add(new BranchProxyView(o));
        }
        return branches;
    }

    public static class BranchProxyView {
        private TreeNode treeNode;
        private String title;

        BranchProxyView(Object[] row) {
            treeNode = (TreeNode) row[0];
            title = (String) row[1];
        }

        @Override
        public String toString() {
            return "BranchProxyView{" +
                    "treeNode=" + treeNode +
                    ", title='" + title + '\'' +
                    '}';
        }
    }
}