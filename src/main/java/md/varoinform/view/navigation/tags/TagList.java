package md.varoinform.view.navigation.tags;

import md.varoinform.controller.cache.Tag;
import md.varoinform.controller.cache.TagCache;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.view.dialogs.TagDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 5/7/14
 * Time: 9:34 AM
 */
public class TagList extends JList<Tag> {
    private String currentTagTitle;

    public TagList() {
        FilteringModel<Tag> model = new FilteringModel<>(TagCache.instance.getTags());
        setModel(model);

        setCellRenderer(new TagListCellRenderer());
        addMouseListener(new MouseAdapter() {
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

    public String getCurrentTagTitle(){
        return currentTagTitle;
    }

    public void setCurrentTagTitle(String currentTagTitle) {
        this.currentTagTitle = currentTagTitle;
    }

    @Override
    public int locationToIndex(Point point) {
        int index = super.locationToIndex(point);
        if (index < 0 || getCellBounds(0, index).getHeight() < point.getY()) return -1;

        return index;
    }

    public boolean addTag(int index, List<Long> enterpriseIds) {
        if (enterpriseIds.isEmpty()) return false;
        if (index >= 0) {
            Tag tag = getModel().getElementAt(index);
            tag.add(enterpriseIds);
            TagCache.instance.updateTag(tag);
            setSelectedIndex(index);
        } else {
            String title = TagDialog.getTag();
            TagCache.instance.addTag(title, enterpriseIds);
            updateModel();
        }
        return true;
    }

    public void renameTag(Tag tag) {
        if (tag == null) return;

        String message = ResourceBundleHelper.getString("rename_tag_message", "Insert new title");
        String newName = (String) JOptionPane.showInputDialog(null, message, "", JOptionPane.QUESTION_MESSAGE, null, null, tag.getTitle());
        if (newName == null || newName.isEmpty() || newName.equals(tag.getTitle())) return;

        tag.setTitle(newName);
        TagCache.instance.updateTag(tag);
        ((FilteringModel<Tag>)getModel()).updateModel();
    }

    public void deleteTag(final Tag tag) {
        if (tag == null) return;

        String message = ResourceBundleHelper.getString("delete_tag", "Delete") + ": " + tag.getTitle() + "?";
        if (JOptionPane.showConfirmDialog(null, message) == JOptionPane.OK_OPTION) {
            ((FilteringModel<Tag>)getModel()).removeElement(tag);
            TagCache.instance.delete(tag);
        }
    }

    public Tag removeFromCurrentTag(List<Long> eids) {
        Tag selectedTag = getSelectedValue();
        boolean clearSelection = selectedTag.remove(eids);
        TagCache.instance.updateTag(selectedTag);
        updateModel();
        if (clearSelection){
            clearSelection();
        }

        return clearSelection? null: selectedTag;
    }


    public void updateModel(){
        FilteringModel<Tag> model = (FilteringModel<Tag>) getModel();
        model.clear();
        model.addAll(TagCache.instance.getTags());
    }
}
