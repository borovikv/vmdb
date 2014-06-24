package md.varoinform.model.dao;

import md.varoinform.controller.comparators.EnterpriseIDComparator;
import md.varoinform.model.entities.Node;
import md.varoinform.model.util.Normalizer;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/5/13
 * Time: 5:33 PM
 */
public class NodeDao extends GenericDaoHibernateImpl<Node, Long >{
    public NodeDao() {
        super(Node.class);
    }

    @SuppressWarnings("unchecked")
    public List<Node> readWithParent(Node node) {
        if (node == null) {
            List nodes = getSession().createCriteria(Node.class).add(Restrictions.eq("id", 1L)).list();
            if (nodes.isEmpty()) return new ArrayList<>();
            node = (Node) nodes.get(0);
            return node.getChildren();
        }


        String hql = "select distinct a.head from Arc a where a.tail = :tail";
        Query query = getSession().createQuery(hql).setParameter("tail", node);
        return query.list();
    }

    public List<Node> startWith(String text) {
        if (text == null || text.isEmpty()){
            return readWithParent(null);
        } else {
            String field = "title.title";
            Normalizer normalizer = new Normalizer(field, text, Normalizer.RO);
            Query query = getSession().createQuery("select distinct tn " +
                    "from Node tn " +
                    "join tn.titles title " +
                    "where " + normalizer.getField() +
                    " like :text");

            query.setString("text", normalizer.getString() + "%");

            @SuppressWarnings("unchecked")
            List<Node> list = query.list();
            return list;
        }
    }

    public List<Long> getEnterpriseIds(Node node){
        String hql = "Select e.id from Node n join n.enterprises e where n.id = :id";
        Query query = getSession().createQuery(hql).setLong("id", node.getId()).setCacheable(false);
        //noinspection unchecked
        List<Long> enterpriseIds = query.list();
        Collections.sort(enterpriseIds, new EnterpriseIDComparator());
        return enterpriseIds;
    }


    public static void main(String[] args) {
        NodeDao nd = new NodeDao();
        List<Node> av = nd.startWith("ав");
        System.out.println(av);
    }
}
