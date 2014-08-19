package md.varoinform.util;

import md.varoinform.Settings;

import java.awt.*;
import java.util.*;
import java.util.List;

public class StringUtils {

    public static String valueOf(Object value) {
        if (value == null) return "";
        if (value instanceof Map) {
            return valueOf(((Map<?, ?>) value).values());
        }
        if(value instanceof Collection<?>) {
            return valueOf((Collection<?>)value);
        }
        if (value instanceof Date) {
            return Settings.getDefaultDateFormat().format(value);
        }
        return String.valueOf(value);
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


    public static Object objectOrString(Object value){
        if (value instanceof Map) {
            return valueOf(((Map<?, ?>) value).values());
        } else if(value instanceof Collection<?>) {
            return valueOf((Collection<?>)value);
        }
        return value;
    }

    public static List<String> wrap(String value, FontMetrics fm, int maxWidth){
        List<String> result = new ArrayList<>();
        if (value.length() == 0) return result;

        if (fm.stringWidth(value) < maxWidth) {
            result.add(value);
            return result;
        }

        String[] split = value.split("[\\r\\n\\s]+");
        String line = "";
        for (String s : split) {
            if (fm.stringWidth(line + " " + s) < maxWidth){
                line += s + " ";
            } else {
                wrap(result, line.trim(), fm, maxWidth);
                line = s;
            }

        }
        wrap(result, line.trim(), fm, maxWidth);
        return result;
    }

    private static void wrap(List<String> result, String line, FontMetrics fm, int maxWidth) {
        if (line.length() > 0) {
            result.addAll(StringWrapper.wrap(line, fm, maxWidth));
        }
    }

}