package md.varoinform.controller.comparators;

import md.varoinform.util.PreferencesHelper;

import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/19/13
 * Time: 2:46 PM
 */
public class ViewPartPriorityComparator implements Comparator<String> {
    PreferencesHelper helper = new PreferencesHelper();
    @Override
    public int compare(String o1, String o2) {
        Integer priority1 = priority(o1);
        Integer priority2 = priority(o2);
        return priority1.compareTo(priority2);
    }

    private Integer priority(String viewPart) {
        List<String> defaultPriority = helper.getDefaultFields();

        for (int i = 0; i < defaultPriority.size(); i++) {
            if (defaultPriority.get(i).equalsIgnoreCase(viewPart)) {
                return i;
            }
        }

        return 1000;
    }
}
