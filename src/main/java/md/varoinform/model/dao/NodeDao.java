package md.varoinform.model.dao;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.entities.Node;
import md.varoinform.model.utils.DefaultClosableSession;
import org.hibernate.Query;
import org.hibernate.Transaction;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/5/13
 * Time: 5:33 PM
 */
public class NodeDao {
    public static Map<Long, List<Long>> getNodeEnterpriseMap(){
        Transaction tx = null;
        try (DefaultClosableSession session = new DefaultClosableSession()) {
            tx = session.beginTransaction();
            String hql2 = "Select distinct new list(n.id, e.id, t.title) " +
                    "from Node n join n.enterprises e join e.titles t " +
                    "where t.language.id = :langID and n.id <> 1 order by t.title";
            Query query1 = session.createQuery(hql2).setLong("langID", LanguageProxy.instance.getCurrentLanguage());
            @SuppressWarnings("unchecked")
            List<List<Object>> list1 = query1.list();

            Map<Long, List<Long>> m = new HashMap<>();
            for (List<Object> l : list1) {
                Long i = (Long) l.get(0);
                List<Long> longs = m.get(i);
                if (longs == null){
                    longs = new ArrayList<>();
                    m.put(i, longs);
                }
                longs.add((Long) l.get(1));
            }

            tx.commit();
            return m;
        } catch (RuntimeException rex){
            if (tx != null) tx.rollback();
            throw rex;
        }

    }

    public static List<Node> getAll(DefaultClosableSession session) {
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

    public static Map<Long, Set<Long>> getArcs(){
        Map<Long, Set<Long>> m = new HashMap<>();
        try (DefaultClosableSession session = new DefaultClosableSession()){
            session.beginTransaction();
            try {
                String hql = "Select arc.tail.id, arc.head.id from Arc arc";
                List list = session.createQuery(hql).list();
                for (Object objects : list) {
                    Object[] ids = (Object[]) objects;
                    Long id = (Long) ids[0];
                    Set<Long> longs = m.get(id);
                    if (longs == null){
                        longs = new HashSet<>();
                        m.put(id, longs);
                    }
                    longs.add((Long) ids[1]);
                }
                session.getTransaction().commit();
            } catch (RuntimeException ignored){
                ignored.printStackTrace();
            }
        }
        return m;
    }
}
