package md.varoinform.controller.comparators;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.entities.Enterprise;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
* Created with IntelliJ IDEA.
* User: Vladimir Borovic
* Date: 6/10/14
* Time: 9:18 AM
*/
public class EnterpriseComparator implements Comparator<Enterprise> {

    private final Collator collator;
    private final String lang;

    public EnterpriseComparator() {
        this(LanguageProxy.instance.getCurrentLanguage());
    }


    public EnterpriseComparator(Long langID) {
        lang = LanguageProxy.instance.getTitle(langID);
        Locale locale = new Locale(LanguageProxy.getCurrentLanguageTitle());
        collator = Collator.getInstance(locale);

    }

    @Override
    public int compare(Enterprise o1, Enterprise o2) {
        String titleO1 = o1.title(lang);
        String titleO2 = o2.title(lang);
        return collator.compare(titleO1, titleO2);
    }
}
