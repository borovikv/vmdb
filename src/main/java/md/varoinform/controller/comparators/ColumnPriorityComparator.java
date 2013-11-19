package md.varoinform.controller.comparators;

import md.varoinform.util.PreferencesHelper;
import java.util.Comparator;

public class ColumnPriorityComparator implements Comparator<String> {
    private final String[] cols;

    public ColumnPriorityComparator() {
        PreferencesHelper preferencesHelper = new PreferencesHelper();
        cols = preferencesHelper.getPrefColumns().split(";");

    }

    @Override
    public int compare(String o1, String o2) {
        Integer priority1 = getPriority(o1);
        Integer priority2 = getPriority(o2);
        if (priority1 == 0 && priority2 == 0) {
            return o1.compareToIgnoreCase(o2);
        } else {
            return priority1.compareTo(priority2);
        }
    }

    private int getPriority(String o) {
        for (int i = 0; i < cols.length; i++) {
            if (o.equalsIgnoreCase(cols[i])) {
                return i + 1;
            }
        }
        return 1000;
    }

}