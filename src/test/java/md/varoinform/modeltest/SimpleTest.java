package md.varoinform.modeltest;

import md.varoinform.model.dao.GenericDaoHibernateImpl;
import md.varoinform.model.dao.TransactionDaoHibernateImpl;
import md.varoinform.model.entities.*;
import md.varoinform.model.util.SessionManager;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/2/14
 * Time: 2:37 PM
 */
public class SimpleTest {
    public static Long max_id = 0L;
    static TransactionDaoHibernateImpl<TreeNode, Long> treeDao = new TransactionDaoHibernateImpl<>(TreeNode.class);
    static TransactionDaoHibernateImpl<NodeTitleContainer, Long> nodeTCDao = new TransactionDaoHibernateImpl<>(NodeTitleContainer.class);
    static TransactionDaoHibernateImpl<NodeTitle, Long> nodeTitleDao = new TransactionDaoHibernateImpl<>(NodeTitle.class);
    static TransactionDaoHibernateImpl<Good2, Long> good2Dao = new TransactionDaoHibernateImpl<>(Good2.class);
    static TransactionDaoHibernateImpl<Good2Title, Long> good2TitleDao = new TransactionDaoHibernateImpl<>(Good2Title.class);
    static Set<Long> nodeIds = new HashSet<>();
    public static void main(String[] args) {
        List<Branch> branches = getBranches();
        System.out.println(branches.size());
        max_id = getMaxBranchId();
        for (Branch branch : branches) {
            if (branch == null) continue;
            Long id = branch.id();

            List<BranchTitle> branchTitles = getBranchTitleByBranchId(id);
            NodeTitleContainer nodeTitleContainer = getNTContainerByTitle(branchTitles);
            if (nodeTitleContainer == null){
                nodeTitleContainer = createNodeTitleContainer(branchTitles);
            }
            TreeNode node = createTreeNode(branch.id(), branch.parentId(), nodeTitleContainer);

            List<Good> goods = getGoodByBranchId(id);
            for (Good good : goods) {
                createGood2(good.getTitles(), node);
            }
        }

    }

    private static List<Branch> getBranches() {
        GenericDaoHibernateImpl<Branch, Long> dao = new GenericDaoHibernateImpl<>(Branch.class);
        return dao.getAll();
    }

    private static Long getMaxBranchId() {
        Session session = SessionManager.getSession();
        Branch maxBranch = (Branch) session.createCriteria(Branch.class).addOrder(Order.desc("id")).setMaxResults(1).uniqueResult();
        return maxBranch.id();
    }

    private static List<BranchTitle> getBranchTitleByBranchId(Long bid){
        return getEntityByProperty(BranchTitle.class, "containerID", bid);
    }

    private static <T> List<T> getEntityByProperty(Class<T> c, String propertyName, Long bid) {
        Session session = SessionManager.getSession();
        Criteria criteria = session.createCriteria(c).add(Restrictions.eq(propertyName, bid));

        //noinspection unchecked
        return criteria.list();
    }

    private static List<Good> getGoodByBranchId(Long bid) {
        return getEntityByProperty(Good.class, "branchID", bid);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static TreeNode  createTreeNode(Long id, Long parentId, NodeTitleContainer title){
        if (nodeExists(id)) {
            max_id++;
            id = max_id;
        }
        TreeNode node = new TreeNode();
        node.setId(id);
        node.setParent(parentId);
        node.setTitle(title);
        SessionManager.getSession().replicate(node, ReplicationMode.EXCEPTION);
        return treeDao.read(node.getId());
    }

    private static boolean nodeExists(Long id){
        boolean contains = nodeIds.contains(id);
        nodeIds.add(id);
        return contains;
//        List<TreeNode> tns = getEntityByProperty(TreeNode.class, "id", id);
//        return tns.size() > 0;
    }

    private static NodeTitleContainer getNTContainerByTitle(List<BranchTitle> titles){
        String table = "EXPORTED_DB.DB_NodeTitle";

        Long id = getContainerByTitles(titles, table);
        if (id == null) return null;
        return nodeTCDao.read(id);
    }

    private static <T extends TitleInterface> Long getContainerByTitles(List<T> titles, String table) {
        if (titles.size() <= 0) return null;

        Session session = SessionManager.getSession();

        String query = createQuery(titles, table);
        Query sessionQuery = session.createSQLQuery(query);
        List list = sessionQuery.list();
        return list.size() >0 ? ((BigInteger)list.get(0)).longValue() : null;
    }

    private static<T extends TitleInterface> String createQuery(List<T> titles, String table) {
        String query = "SELECT t1.container_id " +
                "FROM " + table + " t1  " +
                "join " + table + " t2 on t1.container_id = t2.container_id  " +
                "join " + table + " t3 on t2.container_id = t3.container_id " +
                "where ";

        String operator = "";
        for (int i = 0; i < titles.size(); i++) {
            T title = titles.get(i);
            String t = title.getTitle().replace("'", "@");
            Long l = title.getLanguage().getId();

            String clause = String.format("%s t%d.title = '%s' and  t%2$d.language_id = %d", operator, i+1, t, l);

            operator = " and ";
            query += clause;
        }
        return query;
    }

    private static NodeTitleContainer createNodeTitleContainer(List<BranchTitle> branchTitles){
        NodeTitleContainer container = new NodeTitleContainer();
        nodeTCDao.save(container);
        createNodeTitles(container, branchTitles);
        return container;
    }

    private static void createNodeTitles(NodeTitleContainer container, List<BranchTitle> branchTitles){
        for (BranchTitle branchTitle : branchTitles) {
            NodeTitle title = new NodeTitle();
            title.setLanguage(branchTitle.getLanguage());
            String title1 = branchTitle.getTitle();
            title.setTitle(title1);
            title.setContainer(container);
            nodeTitleDao.save(title);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static Good2 createGood2(List<GoodTitle> goods, TreeNode node ){
        Good2 good2 = getGood2ByGood(goods);
        if (good2 == null){
            good2 = new Good2();
            good2Dao.save(good2);
            createGood2Title(good2, goods);
        }
        Set<TreeNode> treeNode = good2.getTreeNode();
        treeNode.add(node);
        good2.setTreeNode(treeNode);
        good2Dao.save(good2);
        return good2;
    }

    private static Good2 getGood2ByGood(List<GoodTitle> titles){
        String table = "EXPORTED_DB.DB_good2Title";
        Long id = getContainerByTitles(titles, table);
        if (id == null) return null;
        return good2Dao.read(id);
    }

    private static void createGood2Title(Good2 good2, List<GoodTitle> goods){
        for (GoodTitle good : goods) {
            Good2Title title = new Good2Title();
            title.setContainer(good2);
            title.setTitle(good.getTitle());
            title.setLanguage(good.getLanguage());
            good2TitleDao.save(title);
        }
    }



}
