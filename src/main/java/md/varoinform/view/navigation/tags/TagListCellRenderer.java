package md.varoinform.view.navigation.tags;

import md.varoinform.controller.cache.Tag;
import md.varoinform.view.RendererHelper;

import javax.swing.*;
import java.awt.*;

public class TagListCellRenderer implements ListCellRenderer<Tag> {
    RendererHelper helper = new RendererHelper();

    @Override
    public Component getListCellRendererComponent(JList list, Tag value, int index, boolean isSelected, boolean cellHasFocus) {
        helper.setBackground(isSelected, list.getBackground());
        String title = value.getTitle();
        JPanel panel = RendererHelper.getPanel(title);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        return panel;
    }
}