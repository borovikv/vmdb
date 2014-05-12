package md.varoinform.model.dao;

import md.varoinform.model.entities.TreeNode;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/5/13
 * Time: 5:33 PM
 */
public class NodeDao extends GenericDaoHibernateImpl<TreeNode, Long >{
    public NodeDao() {
        super(TreeNode.class);
    }

    @SuppressWarnings("unchecked")
    public List<TreeNode> readWithParent(TreeNode treeNode) {
        if (treeNode == null){
//            getSession().createCriteria(TreeNode.class).add(Restrictions.isNull())
            return getSession().createCriteria(TreeNode.class).add(Restrictions.eq("parent", 1L)).list();
        } else {
            return getSession().createCriteria(TreeNode.class).add(Restrictions.eq("parent", treeNode)).list();
        }
    }

    public List<TreeNode> startWith(String text) {
        if (text == null || text.isEmpty()){
            return readWithParent(null);
        } else {
            Query query = getSession().createQuery("select distinct tn " +
                    "from TreeNode tn " +
                    "join tn.title.titles title " +
                    "where lower(title.title) like :text");
            query.setString("text", text.toLowerCase() + "%");

            @SuppressWarnings("unchecked")
            List<TreeNode> list = query.list();
            return list;
        }
    }

    public static void main(String[] args) {
        NodeDao nd = new NodeDao();
        List<TreeNode> av = nd.startWith("ав");
        System.out.println(av);
    }
}
