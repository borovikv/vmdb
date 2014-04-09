package md.varoinform.model.dao;

import md.varoinform.model.entities.TreeNode;

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
        BranchDao dao = new BranchDao();
        return dao.read(1L);

    }

}
