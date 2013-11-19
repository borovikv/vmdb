package i18n;

import java.util.ListResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/11/13
 * Time: 4:34 PM
 */
public class Strings_en extends ListResourceBundle {
    private static final Object[][] contents = {
            {"treebranch", "goods"},
            {"selected", "selected"},
            {"home", "home"},
            {"back", "back"},
            {"forward", "forward"},
            {"search", "search"},
            {"advancedsearch‎", "advanced search"},
            {"orderby", "order by"},
            {"print", "print"},
            {"export", "export"},
            {"mail", "email"},
            {"settings", "settings"},
            {"result", "result"},
            {"contactperson", "contact person" },
            {"entitytype", "type" },
            {"creation", "creation date" },
            {"foreingcapital", "foreing capital" },
            {"workplaces", "workplaces" },
            {"brands", "brands" },
            {"goods", "goods" },
            {"branches", "branches" },
            {"checkdate", "check date" },
            {"postalcode", "postal code" },
            {"address", "address" },
            {"phones", "phones" },
            {"faxes", "fax" },
            {"emails", "email" },
            {"urls", "urls" },
            {"null", "n/a"},
            {"true", "yes"},
            {"false", "no"},
            {"title", "title"},
            {"creationdate", "creation date"},
            {"businessentitytype", "business entity type"}
    };

    @Override
    protected Object[][] getContents() {
        return contents;
    }
}
