package md.varoinform.model.util;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 5/30/14
 * Time: 11:29 AM
 */
public class Normalizer {
    public static final List<NormalizerChar> RO = Arrays.asList(
            new NormalizerChar('î', 'i'),
            new NormalizerChar('ă', 'a'),
            new NormalizerChar('ţ', 't'),
            new NormalizerChar('ș', 's'),
            new NormalizerChar('â', 'a')
    );
    private final String field;
    private final String string;

    public Normalizer(String field, String string, List<NormalizerChar> chars) {
        this.field = normalizeField(field, chars);
        this.string = normalizeString(string, chars);
    }

    private static String normalizeField(String field, List<NormalizerChar> chars) {
        String result = "Lower(" + field + ")";
        String format = "Replace(%s, '%s', '%s')";
        for (NormalizerChar normalizerChar : chars) {
            result = String.format(format, result, normalizerChar.getOldChar(), normalizerChar.getNewChar());
        }
        return result;
    }

    private static String normalizeString(String string, List<NormalizerChar> chars) {
        String result = string.trim().toLowerCase();
        result = result.replaceAll("\\s+", "%");
        for (NormalizerChar aChar : chars) {
            result = result.replace(aChar.getOldChar(), aChar.getNewChar());
        }
        return result;
    }

    public String getField() {
        return field;
    }

    public String getString() {
        return string;
    }
}
