package md.varoinform.view.tags;

import md.varoinform.Settings;
import md.varoinform.model.dao.DAOTag;
import md.varoinform.model.entities.Tag;
import md.varoinform.util.*;
import md.varoinform.util.Observable;
import md.varoinform.util.Observer;
import md.varoinform.view.NavigationPaneList;

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
public class TagPanel extends JPanel implements Observer, NavigationPaneList, Observable{

    private final AutoCompleteTextField textField;
    private final DAOTag daoTag;
    private final JList<Tag> tagList = new JList<>();
    private String currentTagTitle;
    private List<Observer> observers = new ArrayList<>();
    private boolean programatically = false;

    public TagPanel() {
        setLayout(new BorderLayout());
        textField = new AutoCompleteTextField();
        textField.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        textField.getDocument().addDocumentListener(new MyDocumentListener());
        add(textField, BorderLayout.NORTH);

        daoTag = new DAOTag();
        List<Tag> tags = daoTag.getAll();
        FilteringModel<Tag> model = new FilteringModel<>(tags);
        tagList.setModel(model);
        tagList.setCellRenderer(new TagListCellRenderer());
        tagList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) return;

                Tag tag = getSelectedTag();
                if (tag == null) return;

                setCurrentTagTitle(tag.getTitle());

                if (!programatically) notifyObservers(new ObservableEvent(ObservableEvent.TAG_SELECTED));
            }
        });

        tagList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JList list = (JList) e.getSource();
                int index = list.locationToIndex(e.getPoint());
                if (index >= 0){
                    list.clearSelection();
                    list.setSelectedIndex(index);
                }
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
                    deleteTag(getSelectedTag());
                }
            }
        });

        add(new JScrollPane(tagList), BorderLayout.CENTER);
    }

    private void showPopupMenu(MouseEvent e) {
        if (!e.isPopupTrigger()) return;

        Point point = e.getPoint();
        int index = tagList.locationToIndex(point);
        if (index < 0) return;

        Rectangle cellBounds = tagList.getCellBounds(0, index);
        if (cellBounds.getHeight() < point.getY()) return;

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
                renameTag(tag);
            }
        });

        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(renameItem);
        popupMenu.addSeparator();
        popupMenu.add(deleteItem);
        popupMenu.show(e.getComponent(), e.getX(), e.getY());

    }

    private void renameTag(Tag tag) {
        if (tag == null) return;
        String message = ResourceBundleHelper.getString("rename_tag_message", "Insert new title");
        String result = getNewTitle(tag, message);
        if (result == null || result.isEmpty() || result.equals(tag.getTitle())) return;
        tag.setTitle(result);
        ((FilteringModel<Tag>)tagList.getModel()).updateModel();
        daoTag.save(tag);

    }

    private String getNewTitle(Tag tag, String message) {
        return (String) JOptionPane.showInputDialog(null, message, "", JOptionPane.QUESTION_MESSAGE, null, null, tag.getTitle());
    }

    private void deleteTag(Tag tag) {
        if (tag == null) return;

        String message = ResourceBundleHelper.getString("delete_tag", "Delete") + ": " + tag.getTitle() + "?";
        if (JOptionPane.showConfirmDialog(null, message) == JOptionPane.OK_OPTION) {
            ((FilteringModel<Tag>)tagList.getModel()).removeElement(tag);
            notifyObservers(new ObservableEvent(ObservableEvent.TAGS_CHANGED));
            daoTag.delete(tag);
        }
    }

    public String getCurrentTagTitle(){
        return currentTagTitle;
    }

    private void setCurrentTagTitle(String currentTagTitle) {
        this.currentTagTitle = currentTagTitle;
    }

    public Tag getSelectedTag() {
        return tagList.getSelectedValue();
    }

    @Override
    public void updateSelection(){
        select(getSelectedTag());
    }

    public void select(Tag tag){
        programatically = true;
        int index = ((FilteringModel<Tag>)tagList.getModel()).getIndexAtElement(tag);
        if (index >= 0) {
            clearSelection();
            tagList.setSelectedIndex(index);
        }
        programatically = false;
    }

    public void clearSelection(){
        tagList.clearSelection();
    }

    @Override
    public void update(ObservableEvent event) {
        if (event.getType() == ObservableEvent.TAGS_CHANGED || event.getType() == ObservableEvent.DELETE){
            updateModel();
            if (event.getValue() != null && (Boolean)event.getValue()){
                clearSelection();
            }
        }
    }

    public void updateModel(){
        FilteringModel<Tag> model = (FilteringModel<Tag>) tagList.getModel();
        model.clear();
        model.addAll(daoTag.getAll());
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

    private class MyDocumentListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            filter(e);
        }

        private void filter(DocumentEvent e) {
            currentTagTitle = getLastSearch(e.getDocument());
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
