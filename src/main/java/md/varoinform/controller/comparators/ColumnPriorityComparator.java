package md.varoinform.controller.comparators;

import md.varoinform.util.PreferencesHelper;
import java.util.Comparator;
import java.util.List;

public class ColumnPriorityComparator implements Comparator<String> {
    private final List<String> cols;

    public ColumnPriorityComparator() {
        PreferencesHelper preferencesHelper = new PreferencesHelper();
        cols = preferencesHelper.getUserFields();

    }

    @Override
    public int compare(String o1, String o2) {
        Integer priority1 = getPriority(o1);
        Integer priority2 = getPriority(o2);
        return priority1.compareTo(priority2);
    }

    private int getPriority(String o) {

        for (int i = 0; i < cols.size(); i++) {
            if (o.equalsIgnoreCase(cols.get(i))) {

                return i + 1;
            }
        }

        return 1000;
    }

}