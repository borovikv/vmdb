package md.varoinform.util;

import md.varoinform.model.entities.Language;
import java.util.Collection;
import java.util.Map;

@SuppressWarnings("ALL")
public class StringUtils {

    public static String getStringOrNA(String s, Language language) {

        return s == null || s.isEmpty() ? ResourceBundleHelper.getString(language, "null", "") : s;
    }

    public static String strip(Object value) {
        String str = String.valueOf(value).replaceAll("null", "");
        return org.apache.commons.lang.StringUtils.strip(str, "[], ");
    }

    public static String valueOf(Collection<?> collection){
        StringBuilder builder = new StringBuilder();
        String delimiter = "";
        for (Object title : collection) {
            if (title == null || String.valueOf(title).isEmpty()) continue;
            builder.append(delimiter);
            builder.append(title);
            delimiter = ", ";
        }
        return builder.toString();
    }

    public static String valueOf(Object value) {
        if (value == null) return "";
        if (value instanceof Map) {
            return valueOf(((Map<?, ?>) value).values());
        } else if(value instanceof Collection<?>) {
            return valueOf((Collection<?>)value);
        } else {
            return String.valueOf(value);
        }
    }
}