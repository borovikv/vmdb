package md.varoinform.controller.comparators;

import md.varoinform.controller.entityproxy.EnterpriseProxy;
import md.varoinform.util.StringUtils;

import java.util.Comparator;
import java.util.Date;

/**
* Created with IntelliJ IDEA.
* User: Vladimir Borovic
* Date: 6/19/14
* Time: 9:50 AM
*/
public class EnterpriseProxyComparator implements Comparator<EnterpriseProxy> {

    private String name;
    private boolean isAscending;

    public EnterpriseProxyComparator(String name, boolean isAscending) {
        this.name = name;
        this.isAscending = isAscending;
    }

    @Override
    public int compare(EnterpriseProxy o1, EnterpriseProxy o2) {
        Object o3 = o1.get(name);
        Object o4 = o2.get(name);
        if (o3 == null && o4 == null) return 0;
        if ((o3 == null && isAscending) || (o4 == null && !isAscending)) return -1;
        if (o3 == null || o4 == null) return 1;

        Class<?> cls = EnterpriseProxy.getType(name);
        if (Date.class.isAssignableFrom(cls) || Number.class.isAssignableFrom(cls)) {
            if (isAscending) {
                //noinspection unchecked
                return ((Comparable) o3).compareTo(o4);
            } else {
                //noinspection unchecked
                return -((Comparable) o3).compareTo(o4);
            }
        } else if (isAscending) {
            return StringUtils.valueOf(o3).compareTo(StringUtils.valueOf(o4));
        } else {
            return -StringUtils.valueOf(o3).compareTo(StringUtils.valueOf(o4));
        }
    }
}
