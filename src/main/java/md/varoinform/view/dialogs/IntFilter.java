package md.varoinform.view.dialogs;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 7/25/14
 * Time: 9:32 AM
 */
public class IntFilter extends DocumentFilter {
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        super.insertString(fb, offset, filter(string), attr);
    }

    public String filter(String string) {
        if (string == null) return null;
        StringBuilder builder = new StringBuilder(string);
        for (int i = builder.length() - 1; i >= 0; i--) {
            int cp = builder.codePointAt(i);
            if (!Character.isDigit(cp)){
                builder.deleteCharAt(i);
                if (Character.isSupplementaryCodePoint(cp)){
                    i--;
                    builder.deleteCharAt(i);
                }
            }

        }
        return builder.toString();
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        super.replace(fb, offset, length, filter(text), attrs);
    }
}
