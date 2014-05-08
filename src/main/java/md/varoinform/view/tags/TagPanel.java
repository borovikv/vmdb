package md.varoinform.view.tags;

import md.varoinform.Settings;
import md.varoinform.model.entities.Tag;
import md.varoinform.util.*;
import md.varoinform.util.Observable;
import md.varoinform.util.Observer;
import md.varoinform.view.demonstrator.EnterpriseTransferableHandler;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/29/13
 * Time: 11:34 AM
 */
public class TagPanel extends JPanel implements Observer, Observable{

    private final AutoCompleteTextField textField;
    private final TagList tagList = new TagList();
    private List<Observer> observers = new ArrayList<>();


    public TagPanel() {
        setLayout(new BorderLayout());
        textField = new AutoCompleteTextField();
        textField.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        textField.getDocument().addDocumentListener(new MyDocumentListener());
        add(textField, BorderLayout.NORTH);

        tagList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) return;

                Tag tag = getSelectedTag();
                if (tag == null) return;

                tagList.setCurrentTagTitle(tag.getTitle());
                notifyObservers(new ObservableEvent(ObservableEvent.TAG_SELECTED));
            }
        });

        tagList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                showPopupMenu(e);
            }


            @Override
            public void mousePressed(MouseEvent e) {
                 showPopupMenu(e);
            }
        });

        tagList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getExtendedKeyCode() == KeyEvent.VK_DELETE) {
                    Tag tag = getSelectedTag();
                    deleteTag(tag);
                }
            }
        });

        tagList.setTransferHandler(new EnterpriseTransferableHandler());
        tagList.setDropMode(DropMode.ON_OR_INSERT);

        add(new JScrollPane(tagList), BorderLayout.CENTER);
    }


    private void showPopupMenu(MouseEvent e) {
        if (!e.isPopupTrigger()) return;

        Point point = e.getPoint();
        int index = tagList.locationToIndex(point);
        if (index < 0) return;

        tagList.setSelectedIndex(index);
        final Tag tag = tagList.getModel().getElementAt(index);
        Font font = Settings.getDefaultFont("SERIF", 14);

        String deleteText = ResourceBundleHelper.getString("delete_tag", "Delete") + ": " + tag + "...";
        JMenuItem deleteItem = new JMenuItem(deleteText);
        KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
        deleteItem.setAccelerator(keyStroke);
        deleteItem.setFont(font);
        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTag(tag);
            }
        });

        String renameText = ResourceBundleHelper.getString("rename_tag", "Rename") + "...";
        JMenuItem renameItem = new JMenuItem(renameText);
        KeyStroke renameKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK);
        renameItem.setAccelerator(renameKeyStroke);
        renameItem.setFont(font);
        renameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tagList.renameTag(tag);
            }
        });

        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(renameItem);
        popupMenu.addSeparator();
        popupMenu.add(deleteItem);
        popupMenu.show(e.getComponent(), e.getX(), e.getY());

    }


    public Tag getSelectedTag() {
        return tagList.getSelectedValue();
    }


    private void deleteTag(Tag tag) {
        tagList.deleteTag(tag);
        notifyObservers(new ObservableEvent(ObservableEvent.TAGS_CHANGED));
    }


    @Override
    public void update(ObservableEvent event) {
        if (event.getType() == ObservableEvent.TAGS_CHANGED || event.getType() == ObservableEvent.DELETE){
            tagList.updateModel();
            boolean tagNotExist = event.getValue() != null && (Boolean) event.getValue();
            if (tagNotExist){
                clearSelection();
            }
        }
    }

    public void clearSelection(){
        tagList.clearSelection();
    }


    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers(ObservableEvent event) {
        for (Observer observer : observers) {
            observer.update(event);
        }
    }

    public void addOnEnterAction(ActionListener listener){
        textField.addActionListener(listener);
    }

    public void clearFilter() {
        textField.setText("");
    }

    public String getCurrentTagTitle() {
        return tagList.getCurrentTagTitle();
    }

    private class MyDocumentListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            filter(e);
        }

        private void filter(DocumentEvent e) {
            String currentTagTitle = getLastSearch(e.getDocument());
            tagList.setCurrentTagTitle(currentTagTitle);
            ((FilteringModel)tagList.getModel()).filter(currentTagTitle);
        }

        private String getLastSearch(Document document) {
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
}
