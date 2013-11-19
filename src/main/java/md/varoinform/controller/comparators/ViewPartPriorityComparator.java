package md.varoinform.controller.comparators;

import java.util.Comparator;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/19/13
 * Time: 2:46 PM
 */
public class ViewPartPriorityComparator implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        Integer priority1 = priority(o1);
        Integer priority2 = priority(o2);
        return priority1.compareTo(priority2);
    }

    private Integer priority(String viewPart) {
        ResourceBundle bundle = ResourceBundle.getBundle("VaroDB");
        String[] defaultPriority = bundle.getString("defaultColumns").split(";");

        for (int i = 0; i < defaultPriority.length; i++) {
            if (defaultPriority[i].equalsIgnoreCase(viewPart)) {
                return i;
            }
        }

        return 1000;
    }
}
