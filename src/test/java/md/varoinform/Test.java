package md.varoinform;

import md.varoinform.model.dao.GenericDaoHibernateImpl;
import md.varoinform.model.dao.TransactionDaoHibernateImpl;
import md.varoinform.model.entities.NodeTitleContainer;
import md.varoinform.model.entities.TreeNode;
import md.varoinform.model.util.SessionManager;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/7/14
 * Time: 10:36 AM
 */
public class Test {
    public static void main(String[] args) {
        TreeNode tn = new TreeNode();
        tn.setParent(2L);
        GenericDaoHibernateImpl<NodeTitleContainer, Long> c = new GenericDaoHibernateImpl<>(NodeTitleContainer.class);
        tn.setTitle(c.read(1L));
        TransactionDaoHibernateImpl<TreeNode, Long> tdao = new TransactionDaoHibernateImpl<>(TreeNode.class);
        tdao.save(tn);
        System.out.println(tn);
    }
}
