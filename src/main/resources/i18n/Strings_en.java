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
            {"treeBranch", "goods"},
            {"selected", "selected"},
            {"home", "home"},
            {"back", "back"},
            {"forward", "forward"},
            {"search", "search"},
            {"advancedSearch‎", "advanced search"},
            {"orderBy", "order by"},
            {"print", "print"},
            {"export", "export"},
            {"mail", "email"},
            {"settings", "settings"},
            {"result", "result"},
            {"contactPerson", "contact person" },
            {"entityType", "type" },
            {"creation", "creation date" },
            {"foreingCapital", "foreing capital" },
            {"workplaces", "workplaces" },
            {"brands", "brands" },
            {"goods", "goods" },
            {"branches", "branches" },
            {"checkDate", "check date" },
            {"postalCode", "postal code" },
            {"address", "address" },
            {"phone", "phone" },
            {"fax", "fax" },
            {"email", "email" },
            {"urls", "urls" },
            {"null", "n/a"},
            {"true", "yes"},
            {"false", "no"},
            {"title", "title"}
    };

    @Override
    protected Object[][] getContents() {
        return contents;
    }
}
