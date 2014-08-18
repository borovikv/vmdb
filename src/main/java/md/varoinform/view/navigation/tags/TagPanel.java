package md.varoinform.view.navigation.tags;

import md.varoinform.Settings;
import md.varoinform.controller.history.History;
import md.varoinform.controller.history.HistoryEvent;
import md.varoinform.model.entities.Tag;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.util.observer.Observable;
import md.varoinform.util.observer.ObservableEvent;
import md.varoinform.util.observer.ObservableIml;
import md.varoinform.util.observer.Observer;
import md.varoinform.view.demonstrator.EnterpriseTransferableHandler;
import md.varoinform.view.navigation.AutoCompleteTextField;
import md.varoinform.view.navigation.FilteringDocumentListener;
import md.varoinform.view.navigation.FilteringNavigator;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/29/13
 * Time: 11:34 AM
 */
public class TagPanel extends JPanel implements Observer, Observable, FilteringNavigator{

    private final AutoCompleteTextField textField;
    private final TagList tagList = new TagList();
    private ObservableIml observable = new ObservableIml();

    public TagPanel(String placeholder) {
        setLayout(new BorderLayout());
        textField = new AutoCompleteTextField(placeholder);
        textField.getDocument().addDocumentListener(new FilteringDocumentListener(this));
        add(textField, BorderLayout.NORTH);

        tagList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) return;

                Tag tag = getSelectedTag();
                if (tag == null) return;

                tagList.setCurrentTagTitle(tag.getTitle());
                History.instance.add(new HistoryEvent(TagPanel.this, tag));
                notifyObservers(new ObservableEvent<>(ObservableEvent.Type.TAG_SELECTED, tag));
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
        History.instance.addObserver(this);
    }


    private void showPopupMenu(MouseEvent e) {
        if (!e.isPopupTrigger()) return;

        Point point = e.getPoint();
        int index = tagList.locationToIndex(point);
        if (index < 0) return;

        tagList.setSelectedIndex(index);
        final Tag tag = tagList.getModel().getElementAt(index);
        Font font = Settings.getDefaultFont(Settings.Fonts.SANS_SERIF, 14);

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
        notifyObservers(new ObservableEvent<Tag>(ObservableEvent.Type.TAG_SELECTED, null));
    }


    @Override
    public void update(ObservableEvent event) {
        Object value = event.getValue();
        switch (event.getType()){
            case HISTORY_MOVE_FORWARD:
            case HISTORY_MOVE_BACK:
                if (value instanceof HistoryEvent && ((HistoryEvent) value).getSource() == this){
                    tagList.setSelectedValue(((HistoryEvent) value).getState(), true);
                } else {
                    clearSelection();
                }
                break;
        }
    }

    public void deleteEnterpriseFromTag(List<Long> eids) {
        Tag tag = tagList.deleteEnterpriseFromTag(eids);
        notifyObservers(new ObservableEvent<>(ObservableEvent.Type.TAG_SELECTED, tag));
    }

    public void clearSelection(){
        tagList.clearSelection();
    }


    @Override
    public void addObserver(Observer observer) {
        observable.addObserver(observer);
    }

    @Override
    public void notifyObservers(ObservableEvent event) {
        observable.notifyObservers(event);
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

    @Override
    public void filter(String text) {
        tagList.setCurrentTagTitle(text);
        ((FilteringModel)tagList.getModel()).filter(text);
    }

    public void addTag(List<Long> enterpriseIds) {
        tagList.addTag(-1, enterpriseIds);
    }
}
