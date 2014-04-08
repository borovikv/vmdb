package md.varoinform.model.dao;

import md.varoinform.model.entities.TreeNode;
import md.varoinform.model.util.SessionManager;
import org.hibernate.Session;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/5/13
 * Time: 5:33 PM
 */
public class BranchDao extends GenericDaoHibernateImpl<TreeNode, Long >{
    public BranchDao() {
        super(TreeNode.class);
    }

    public static TreeNode getRoot(){
        /*BranchDao dao = new BranchDao();
        return dao.read(0L);*/
        Session session = SessionManager.getSession();
        List list = session.createQuery("from TreeNode where id = 1").list();
        if (list.size() <= 0) return null;
        return (TreeNode) list.get(0);
    }

}
