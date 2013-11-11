package md.varoinform.controller.enterprisecomparators;

import md.varoinform.model.entities.Enterprise;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/7/13
 * Time: 2:24 PM
 */
public class DefaultComparator extends EnterpriseComparator {
    public DefaultComparator() {
        titles.add("по умолчанию");
        titles.add("by default");
    }

    @Override
    public int compare(Enterprise o1, Enterprise o2) {
        return 0;
    }
}
