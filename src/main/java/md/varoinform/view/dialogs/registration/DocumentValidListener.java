package md.varoinform.view.dialogs.registration;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.text.ParseException;

public class DocumentValidListener implements DocumentListener {
    private FormattedTextField field;
    private JButton button;

    public DocumentValidListener(FormattedTextField field, JButton button) {
        this.field = field;
        this.button = button;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        manageEnable(e.getDocument());
    }

    private void manageEnable(Document document) {
        try {
            String text = document.getText(0, document.getLength());

            int textLength = text.replaceAll("\\s+", "").length();
            boolean isFieldContainsSpace = textLength != document.getLength();
            if (isFieldContainsSpace) return;

            field.getFormatter().stringToValue(text);
            button.setEnabled(true);
            field.setInputValid(true);

        } catch (BadLocationException e1) {
            e1.printStackTrace();

        } catch (ParseException e) {
            button.setEnabled(false);
            field.setInputValid(false);
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        manageEnable(e.getDocument());
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        manageEnable(e.getDocument());

    }
}