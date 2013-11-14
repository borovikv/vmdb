import java.awt.*;
import java.util.ListResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/14/13
 * Time: 9:18 AM
 */
public class VaroDB extends ListResourceBundle {
    private static final Object[][] contents = {
            {"highlightColor", new Color(255, 255, 204)},
            {"alternativeColor", new Color(238, 238, 238)}

    };

    @Override
    protected Object[][] getContents() {
        return contents;
    }
}
