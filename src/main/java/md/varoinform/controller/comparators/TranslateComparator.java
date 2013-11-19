package md.varoinform.controller.comparators;

import md.varoinform.util.ResourceBundleHelper;

import java.util.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/19/13
 * Time: 12:48 PM
 */
public class TranslateComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        return ResourceBundleHelper.getString(o1, o1).compareToIgnoreCase(ResourceBundleHelper.getString(o2, o2));
    }
}
