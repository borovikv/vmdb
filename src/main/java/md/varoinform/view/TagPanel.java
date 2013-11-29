package md.varoinform.view;

import md.varoinform.model.dao.DAOTag;
import md.varoinform.model.entities.Tag;
import md.varoinform.view.dialogs.AutoCompleteTextField;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/29/13
 * Time: 11:34 AM
 */
public class TagPanel extends JPanel{

    private final AutoCompleteTextField textField;
    private final DAOTag daoTag;
    private final JList<Tag> tagList = new JList<>();
    private String lastSearch;
    private String currentTag;

    public TagPanel() {
        setLayout(new BorderLayout());
        textField = new AutoCompleteTextField();
        textField.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filter(e);
            }

            private void filter(DocumentEvent e) {
                lastSearch = getLastSearch(e.getDocument());
                ((FilteringModel)tagList.getModel()).filter(lastSearch);
                currentTag = lastSearch;
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
        });
        add(textField, BorderLayout.NORTH);

        daoTag = new DAOTag();
        java.util.List<Tag> tags = daoTag.getAll();
        FilteringModel<Tag> model = new FilteringModel<>(tags);
        tagList.setModel(model);
        tagList.setCellRenderer(new ListCellRenderer<Tag>() {
            RendererHelper helper = new RendererHelper();
            @Override
            public Component getListCellRendererComponent(JList list, Tag value, int index, boolean isSelected, boolean cellHasFocus) {
                helper.setBackground(isSelected, list.getBackground());
                String title = value.getTitle();
                JPanel panel = RendererHelper.getPanel(title);
                panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                return panel;
            }
        });
        ResourceBundle bundle = ResourceBundle.getBundle("VaroDB");
        Color color = (Color)bundle.getObject("highlightColor");
        tagList.setSelectionBackground(color);
        add(new JScrollPane(tagList), BorderLayout.CENTER);
    }

    public String getCurrentTag(){
        return currentTag;
    }

    public void setCurrentTag(String currentTag) {
        this.currentTag = currentTag;
    }

    public void fireTagsChanged(){
        FilteringModel<Tag> model = (FilteringModel<Tag>) tagList.getModel();
        model.clear();
        model.addAll(daoTag.getAll());
    }

    public Tag getSelectedTag() {
        return tagList.getSelectedValue();
    }

    public void addSelectionListener(ListSelectionListener listener){
        tagList.addListSelectionListener(listener);
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
        });
    }

    public void setText(String s) {
        textField.setText(s);
    }

    public void clearSelection(){
        tagList.clearSelection();
    }

    public void addOnEnterAction(ActionListener listener){
        textField.addActionListener(listener);
    }

    public void select(Tag tag){
        int index = ((FilteringModel<Tag>)tagList.getModel()).getIndexAtElement(tag);
        if (index >= 0) {
            tagList.clearSelection();
            tagList.setSelectedIndex(index);
        }
    }

    public void updateSelection(){
        select(getSelectedTag());
    }

}
