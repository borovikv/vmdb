package md.varoinform.util;

import md.varoinform.model.entities.Language;

import java.util.Collection;
import java.util.Map;

public class StringUtils {

    public static String getStringOrNA(String s, Language language) {

        return s == null || s.isEmpty() ? ResourceBundleHelper.getString(language, "null", "") : s;
    }

    public static String getStringFromCollection(Collection<?> value) {
        StringBuilder result = new StringBuilder(String.valueOf(value));
        return result.substring(1, result.length() - 1);
    }

    public static String valueOf(Object value) {

        if (value == null) return "";
        String result;
        if (value instanceof Collection){
            result = getStringFromCollection((Collection<?>) value);
        } else if (value instanceof Map) {
            result = getStringFromCollection(((Map<?,?>)value).values());
        } else {
            result = String.valueOf(value);
        }

        return result;
    }
}