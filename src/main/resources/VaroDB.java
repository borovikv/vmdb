import java.awt.*;
import java.util.ListResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/14/13
 * Time: 9:18 AM
 */
public class VaroDB extends ListResourceBundle {
    private static final String serverUrl = "http://localhost:8000/";
    private static final Object[][] contents = {
            {"highlightColor", new Color(255, 255, 204)},
            {"alternativeColor", new Color(238, 238, 238)},
            {"columns", "title;address;phones;faxes;emails;urls"},
            {"defaultFieldPriority", "title;address;phones;faxes;emails;urls"},
            {"SERIF", new Font(Font.SERIF, Font.PLAIN, 18)},
            {"SANS_SERIF", new Font(Font.SANS_SERIF, Font.PLAIN, 18)},
            {"MONOSPACED", new Font(Font.MONOSPACED, Font.PLAIN, 18)},
            {"register_url", serverUrl + "registry/online/"},
            {"update_url", serverUrl + "manage/update/"}

    };

    @Override
    protected Object[][] getContents() {
        return contents;
    }
}
