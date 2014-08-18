package md.varoinform.model.dao;

import md.varoinform.model.entities.Node;
import md.varoinform.model.util.ClosableSession;
import org.hibernate.Query;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/5/13
 * Time: 5:33 PM
 */
public class NodeDao {

    public List<Long> getEnterprisesID(Node node){
        Transaction tx = null;
        try (ClosableSession session = new ClosableSession()) {
            tx = session.beginTransaction();
            String hql = "Select e.id from Node n join n.enterprises e where n.id = :id";
            Query query = session.createQuery(hql).setLong("id", node.getId()).setCacheable(false);
            //noinspection unchecked
            List<Long> enterpriseIds = query.list();
            tx.commit();
            return enterpriseIds;
        } catch (RuntimeException rex){
            if (tx != null) tx.rollback();
            throw rex;
        }
    }

    public List<Node> getAll(ClosableSession session) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List list = session.createCriteria(Node.class).setCacheable(false).list();
            if (!tx.wasCommitted()){
                tx.commit();
            }
            //noinspection unchecked
            return list;
        } catch (RuntimeException rexc){
            if (tx != null) tx.rollback();
            throw rexc;
        }
    }

    public List<Long> getChildrenID(Long nodeId) {
        try (ClosableSession session = new ClosableSession()){
            String hql = "Select arc.head.id from Arc arc where arc.tail.id = " + nodeId;
            //noinspection unchecked
            return session.createQuery(hql).setCacheable(false).list();
        }
    }
}
