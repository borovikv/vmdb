package md.varoinform.controller.comparators;

import md.varoinform.controller.cache.Cache;
import md.varoinform.controller.LanguageProxy;
import md.varoinform.controller.cache.Field;

import java.text.Collator;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 6/24/14
 * Time: 3:58 PM
 */
public class EnterpriseIDComparator implements java.util.Comparator<Integer> {
    private final Collator collator;

    public EnterpriseIDComparator() {
        Locale locale = new Locale(LanguageProxy.getCurrentLanguageTitle());
        collator = Collator.getInstance(locale);
    }

    @Override
    public int compare(Integer o1, Integer o2) {
        if (o1 == null && o2 == null) return 0;
        if (o1 == null) return -1;
        if (o2 == null) return 1;
        return collator.compare(Cache.instance.getValue(o1, Field.title), Cache.instance.getValue(o2, Field.title));
    }
}
