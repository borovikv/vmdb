package md.varoinform.controller.enterprisecomparators;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.entities.Language;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/7/13
 * Time: 2:26 PM
 */
public abstract class EnterpriseComparator implements Comparator<Enterprise> {
    protected List<String> titles = new ArrayList<>();
    @Override
    public abstract int compare(Enterprise o1, Enterprise o2);

    @Override
    public String toString() {
        Language currentLanguage = LanguageProxy.getInstance().getCurrentLanguage();

        int languageIndex = currentLanguage.getId().intValue() - 1;
        if ( titles.size() >= languageIndex ) {
            return titles.get( languageIndex );

        } else if (titles.size() > 0){
            return titles.get(0);

        }

        return "";
    }
}
