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
            {"result", "result"}
    };

    @Override
    protected Object[][] getContents() {
        return contents;
    }
}
