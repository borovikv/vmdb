package md.varoinform.util;

import md.varoinform.model.entities.Language;

import java.util.Map;

public class StringUtils {

    public static String getStringOrNA(String s, Language language) {

        return s == null || s.isEmpty() ? ResourceBundleHelper.getString(language, "null", "") : s;
    }

    public static String strip(Object value) {
        String str = String.valueOf(value).replaceAll("null", "");
        return org.apache.commons.lang.StringUtils.strip(str, "[], ");
    }

    public static String valueOf(Object value) {
        if (value == null) return "";
        if (value instanceof Map) {
            return strip(((Map<?, ?>) value).values());
        } else {
            return strip(value);
        }
    }
}