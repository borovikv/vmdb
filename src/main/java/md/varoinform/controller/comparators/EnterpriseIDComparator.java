package md.varoinform.controller.comparators;

import md.varoinform.controller.Cache;
import md.varoinform.model.entities.Enterprise;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 6/24/14
 * Time: 3:58 PM
 */
public class EnterpriseIDComparator implements java.util.Comparator<Long> {
    @Override
    public int compare(Long o1, Long o2) {
        if (o1 == null && o2 == null) return 0;
        if (o1 == null) return -1;
        if (o2 == null) return 1;
        Enterprise e1 = Cache.instance.getEnterprise(o1);
        Enterprise e2 = Cache.instance.getEnterprise(o2);
        if (e1 == e2) return 0;
        if (e1 == null) return -1;
        if (e2 == null) return 1;
        return e1.compareTo(e2);
    }
}
