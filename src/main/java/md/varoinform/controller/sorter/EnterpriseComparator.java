package md.varoinform.controller.sorter;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.entities.Language;

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
    @Override
    public int compare(Enterprise o1, Enterprise o2) {
        Language lang = LanguageProxy.instance.getCurrentLanguage();
        Locale locale = new Locale(LanguageProxy.getCurrentLanguageTitle());
        Collator collator = Collator.getInstance(locale);
        String titleO1 = o1.title(lang);
        String titleO2 = o2.title(lang);
        return collator.compare(titleO1, titleO2);
    }
}
