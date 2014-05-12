package md.varoinform.view.navigation;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
* Created with IntelliJ IDEA.
* User: Vladimir Borovic
* Date: 5/12/14
* Time: 10:58 AM
*/
public class FilteringDocumentListener implements DocumentListener {
    private final FilteringNavigator navigator;

    public FilteringDocumentListener(FilteringNavigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        filter(e);
    }

    private void filter(DocumentEvent e) {
        String text = getText(e.getDocument());
        navigator.filter(text);
    }

    private String getText(Document document) {
        try {
            return document.getText(0, document.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        filter(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }
}
