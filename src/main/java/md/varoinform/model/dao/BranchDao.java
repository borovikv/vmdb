package md.varoinform.model.dao;

import md.varoinform.model.entities.Branch;
import md.varoinform.model.util.SessionManager;
import org.hibernate.Session;

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
        /*BranchDao dao = new BranchDao();
        return dao.read(0L);*/
        Session session = SessionManager.getSession();
        return (Branch) session.createQuery("from Branch where id = 0").list().get(0);
    }

}
