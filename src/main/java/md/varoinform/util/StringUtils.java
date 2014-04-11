package md.varoinform.util;

import java.util.Collection;
import java.util.Map;

public class StringUtils {

    public static String getStringOrNA(String s) {

        return s == null || s.isEmpty() ? ResourceBundleHelper.getString("null") : s;
    }

    public static String getStringFromCollection(Collection<?> value) {
            String result = String.valueOf(value);
        return result.substring(1, result.length() - 1);
    }

    public static String valueOf(Object value) {
        if (value == null) return "";
        String result;
        if (value instanceof Collection){
            result = StringUtils.getStringFromCollection((Collection<?>) value);
        } else if (value instanceof Map) {
            result = StringUtils.getStringFromCollection(((Map<?,?>)value).values());
        } else {
            result = String.valueOf(value);
        }
        return result;
    }
}