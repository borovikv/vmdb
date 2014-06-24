package md.varoinform.view.navigation.tags;

import md.varoinform.controller.Cache;
import md.varoinform.model.dao.EnterpriseDao;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.entities.Tag;
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
        FilteringModel<Tag> model = new FilteringModel<>(Cache.instance.getTags());
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
        List<Enterprise> enterprises = new EnterpriseDao().read(enterpriseIds);
        if (index >= 0) {
            Tag tag = getModel().getElementAt(index);
            Cache.instance.saveTag(tag, enterprises);
            setSelectedIndex(index);
        } else {
            String title = TagDialog.getTag();
            Cache.instance.createTag(title, enterprises);
            updateModel();
        }
        return true;
    }

    public void renameTag(Tag tag) {
        if (tag == null) return;
        String message = ResourceBundleHelper.getString("rename_tag_message", "Insert new title");
        String result = getNewTitle(tag, message);
        if (result == null || result.isEmpty() || result.equals(tag.getTitle())) return;
        tag.setTitle(result);
        ((FilteringModel<Tag>)getModel()).updateModel();
        Cache.instance.saveTag(tag);

    }

    public String getNewTitle(Tag tag, String message) {
        return (String) JOptionPane.showInputDialog(null, message, "", JOptionPane.QUESTION_MESSAGE, null, null, tag.getTitle());
    }

    public void deleteTag(final Tag tag) {
        if (tag == null) return;

        String message = ResourceBundleHelper.getString("delete_tag", "Delete") + ": " + tag.getTitle() + "?";
        if (JOptionPane.showConfirmDialog(null, message) == JOptionPane.OK_OPTION) {
            ((FilteringModel<Tag>)getModel()).removeElement(tag);
            Cache.instance.delete(tag);
        }
    }


    public void updateModel(){
        FilteringModel<Tag> model = (FilteringModel<Tag>) getModel();
        model.clear();
        model.addAll(Cache.instance.getTags());
    }


}
