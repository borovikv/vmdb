package md.varoinform.modeltest.util;

import md.varoinform.model.entities.Branch;
import md.varoinform.model.util.HibernateSessionFactory;
import org.hibernate.Query;

import java.util.ArrayList;
import java.util.List;

public class BranchProxy {
    public BranchProxy() {
        
    }

    // Select branch, branch_title
    public static List<BranchProxyView> getBranchProxyView(Long language_id, Long parent_id) {

        String hql = "select b, bt.title from Branch b, BranchTitle bt " +
                "where " +
                "b.parent.id = " + parent_id +
                " and " +
                "b.id = bt.container " +
                "and " +
                "bt.language.id = :language_id " +
                "order by bt.title";

        Query query = HibernateSessionFactory.getSession().createQuery(hql)
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
        private Branch branch;
        private String title;

        BranchProxyView(Object[] row) {
            branch = (Branch) row[0];
            title = (String) row[1];
        }

        public BranchProxyView(Branch branch) {
            this.branch = branch;
        }

        public Branch getBranch() {
            return branch;
        }

        public String getTitle() {
            return title;
        }

        public Long getId(){
            return branch.getId();
        }

        @Override
        public String toString() {
            return "BranchProxyView{" +
                    "branch=" + branch +
                    ", title='" + title + '\'' +
                    '}';
        }
    }
}