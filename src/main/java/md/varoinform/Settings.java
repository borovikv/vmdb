package md.varoinform;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 3/27/14
 * Time: 10:25 AM
 */
public class Settings {
    private static final String SERVER_URL = "http://localhost:8000/";
    private static final String REGISTER_URL =  SERVER_URL + "registry/online/";
    private static final String UPDATE_URL = SERVER_URL + "manage/update/";
    private static final String DEFAULT_COLUMNS = "title;town;StreetHouseOffice;phones;faxes;emails;urls";
   // {"defaultFieldPriority", "title;address;phones;faxes;emails;urls"},


    public static String getRegisterUrl() {
        return REGISTER_URL;
    }

    public static String getUpdateUrl() {
        return UPDATE_URL;
    }

    public static String getDefaultColumns() {
        return DEFAULT_COLUMNS;
    }

    public static Font getDefaultFont(String name){
        switch (name){
            case "SERIF":
                return new Font(Font.SERIF, Font.PLAIN, 18);
            case "SANS_SERIF":
                return new Font(Font.SANS_SERIF, Font.PLAIN, 18);
            case "MONOSPACED":
                return new Font(Font.MONOSPACED, Font.PLAIN, 18);
            default:
                return new Font(Font.SERIF, Font.PLAIN, 18);
        }
    }

    public static Color getDefaultColor(String name){
        switch (name){
            case "highlight":
                return  new Color(255, 255, 204);
            case "alternative":
                return new Color(238, 238, 238);
            default:
                return null;
        }
    }

    public static String getWorkFolder(){
        Path path = null;
        try {
            URI uri = Settings.class.getProtectionDomain().getCodeSource().getLocation().toURI();
            path = Paths.get(uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();

        }

        if (path != null && path.endsWith(".jar")){
            return path.getParent().toString();
        }
        return System.getProperty("user.dir");
    }

    public static Path pathToDB(){
        Path path = Paths.get(Settings.getWorkFolder(), "database", "DB");
        if (Files.notExists(path.getParent())) throw new RuntimeException("file " + path.toAbsolutePath().toString() + " not found");
        return path;
    }
}
