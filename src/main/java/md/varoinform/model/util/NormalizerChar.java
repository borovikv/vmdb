package md.varoinform.model.util;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 5/30/14
 * Time: 11:11 AM
 */
public class NormalizerChar {
    private final char oldChar;
    private final char newChar;

    public NormalizerChar(char oldChar, char newChar) {
        this.oldChar = oldChar;
        this.newChar = newChar;
    }

    public char getOldChar() {
        return oldChar;
    }

    public char getNewChar() {
        return newChar;
    }
}
