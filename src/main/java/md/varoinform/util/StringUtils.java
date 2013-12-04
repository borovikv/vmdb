package md.varoinform.util;

public class StringUtils {
    public static String getStringOrNA(String s) {
        return s == null || s.isEmpty() ? ResourceBundleHelper.getString("null") : s;
    }
}