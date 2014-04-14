package md.varoinform.view.tags;

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
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
                programatically = true;
                JList list = (JList) e.getSource();
                int index = list.locationToIndex(e.getPoint());
                if (index >= 0){
                    list.clearSelection();
                    list.setSelectedIndex(index);
                }
                programatically = false;
            }
        });
        add(new JScrollPane(tagList), BorderLayout.CENTER);
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
