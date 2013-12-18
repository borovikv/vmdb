package md.varoinform.view.dialogs.registration;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class MyDocumentListener implements DocumentListener {
    private FormattedTextField field;
    private JButton button;

    public MyDocumentListener(FormattedTextField field, JButton button) {
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
            boolean b = textLength == document.getLength() && field.isEditValid();
            button.setEnabled(b);
            field.setInputValid(b);
        } catch (BadLocationException e1) {
            e1.printStackTrace();
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