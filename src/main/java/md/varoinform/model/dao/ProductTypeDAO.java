package md.varoinform.model.dao;

import md.varoinform.model.entities.product.ProductType;
import md.varoinform.model.utils.DefaultClosableSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vladimir on 29.04.15.
 *
 */
public class ProductTypeDAO {
    public static Map<Integer, String> getTypes() {
        if (types.keySet().size() != 0) {
            return types;
        }
        try (DefaultClosableSession session = new DefaultClosableSession()){
            @SuppressWarnings("unchecked") List<ProductType> list = session.createCriteria(ProductType.class).list();
            for (ProductType productType : list) {
                types.put(productType.getId(), productType.getTitle());
            }
        }
        return types;
    }

    private static final Map<Integer, String> types = new HashMap<>();
}
