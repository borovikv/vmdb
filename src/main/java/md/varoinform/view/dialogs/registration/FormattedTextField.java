package md.varoinform.view.dialogs.registration;

import md.varoinform.Settings;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/18/13
 * Time: 11:18 AM
 */
public class FormattedTextField extends JFormattedTextField {
    private final String mask;
    private boolean isInputValid = false;

    public FormattedTextField (int length) {
        mask = getMask(length);
        setFormatterFactory(new AbstractFormatterFactory() {
            @Override
            public AbstractFormatter getFormatter(JFormattedTextField tf) {
                 return formatter(mask);
            }
        });
        Font font = Settings.getDefaultFont("MONOSPACED");
        setFont(font);

        Dimension preferredSize = getFieldSize(font);
        setPreferredSize(preferredSize);

        Border insideBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        Border outsideBorder = BorderFactory.createEtchedBorder();
        setBorder(BorderFactory.createCompoundBorder(outsideBorder, insideBorder));
    }

    private Dimension getFieldSize(Font font) {
        FontMetrics fontMetrics = getFontMetrics(font);
        int textFieldWidth = fontMetrics.stringWidth(mask);
        //Empty border width + EtchedBorderWidth
        int borderWidth = 5 + 3;
        return new Dimension(textFieldWidth + borderWidth*2, fontMetrics.getHeight() + borderWidth*2);
    }


    public boolean isValueValid() {
        return isInputValid;
    }

    public void setInputValid(boolean b){
        isInputValid = b;
    }

    private MaskFormatter formatter(String mask) {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter(mask);
            formatter.setPlaceholder(" ");
            formatter.setValueContainsLiteralCharacters(false);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatter;
    }

    private String getMask(int length) {
        String mask = "HHHH";
        for (int i = 0; i < length-1; i++) {
            mask += ".HHHH";
        }
        return mask;
    }

    public void addDocumentListener(DocumentListener listener){
        getDocument().addDocumentListener(listener);
    }
}
