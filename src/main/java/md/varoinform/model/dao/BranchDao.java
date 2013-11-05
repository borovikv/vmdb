package md.varoinform.model.dao;

import md.varoinform.model.entities.Branch;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/5/13
 * Time: 5:33 PM
 */
public class BranchDao extends GenericDaoHibernateImpl< Branch, Long >{
    public BranchDao() {
        super(Branch.class);
    }

    public static Branch getRoot(){
        BranchDao dao = new BranchDao();
        return dao.read(1L);
    }

}
