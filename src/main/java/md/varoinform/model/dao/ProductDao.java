package md.varoinform.model.dao;

import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.entities.Product;
import md.varoinform.model.utils.DefaultClosableSession;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by vladimir on 22.04.16.
 */
public class ProductDao {
    public static List<Integer> getEnterprises(Integer node) {
        try(DefaultClosableSession session = new DefaultClosableSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                List<Enterprise> enterprises = ((Product) session.getSession().get(Product.class, node)).gEnterprises();
                List<Integer> eids = new ArrayList<>();
                for (Enterprise e: enterprises) {
                    eids.add(e.getId());
                }
                return eids;
            } catch (RuntimeException e) {
                e.printStackTrace();
                transaction.rollback();
                throw new NoSuchElementException();
            }
        }

    }

    public static List<Integer> getChildren(Integer id) {
        return null;
    }

    public static String getTitle(Integer id) {
        return null;
    }

    public static List<Integer> startWith(String text) {
        return null;
    }
}
