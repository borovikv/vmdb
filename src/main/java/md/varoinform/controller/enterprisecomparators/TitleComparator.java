package md.varoinform.controller.enterprisecomparators;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.entities.Language;

public class TitleComparator extends EnterpriseComparator {

    public TitleComparator() {
        titles.add("по имени");
        titles.add("by title");
    }

    @Override
    public int compare(Enterprise o1, Enterprise o2) {
        Language language = LanguageProxy.getInstance().getCurrentLanguage();
        return o1.title(language).toLowerCase().compareTo(o2.title(language).toLowerCase());
    }

}