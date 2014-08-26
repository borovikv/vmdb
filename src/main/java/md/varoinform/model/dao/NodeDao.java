package md.varoinform.model.dao;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.entities.Node;
import md.varoinform.model.util.ClosableSession;
import org.hibernate.Query;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/5/13
 * Time: 5:33 PM
 */
public class NodeDao {

    public static List<Long> getEnterprisesID(Long id){
        Transaction tx = null;
        try (ClosableSession session = new ClosableSession()) {
            tx = session.beginTransaction();
            String hql = "Select distinct new list(e.id as id, t.title) " +
                    "from Node n join n.enterprises e join e.titles t " +
                    "where n.id = :id and t.language.id = :lang order by t.title";
            Query query = session.createQuery(hql)
                    .setLong("id", id)
                    .setLong("lang", LanguageProxy.instance.getCurrentLanguage())
                    .setCacheable(false);
            //noinspection unchecked
            List<List<Object>> list = query.list();
            List<Long> enterpriseIds = new ArrayList<>();
            for (List<Object> objects : list) {
                enterpriseIds.add((Long) objects.get(0));
            }
            tx.commit();
            return enterpriseIds;
        } catch (RuntimeException rex){
            if (tx != null) tx.rollback();
            throw rex;
        }
    }

    public static List<Node> getAll(ClosableSession session) {
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

    public static List<Long> getChildrenID(Long nodeId) {
        try (ClosableSession session = new ClosableSession()){
            String hql = "Select arc.head.id from Arc arc where arc.tail.id = " + nodeId;
            //noinspection unchecked
            return session.createQuery(hql).setCacheable(false).list();
        }
    }
}
