package md.varoinform;

import md.varoinform.util.ImageHelper;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
    private static final DateFormat df = new SimpleDateFormat("dd/MM/yy");
    private static final Image MAIN_ICON = ImageHelper.getImageIcon("/external-resources/icons/V.png").getImage();

    public static enum Fonts {
        SANS_SERIF(Font.SANS_SERIF), MONOSPACED(Font.MONOSPACED);

        private final String name;

        Fonts(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }


    public static String getRegisterUrl() {
        return REGISTER_URL;
    }

    public static String getUpdateUrl() {
        return UPDATE_URL;
    }

    public static String getDefaultColumns() {
        return DEFAULT_COLUMNS;
    }

    public static Font getDefaultFont(Fonts name){
        return getDefaultFont(name, 18);
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

    public static Font getDefaultFont(Fonts name, int size) {
        return new Font(name.getName(), Font.PLAIN, size);
    }

    public static DateFormat getDefaultDateFormat() {
        return df;
    }

    public static Image getMainIcon() {
        return MAIN_ICON;
    }
}
