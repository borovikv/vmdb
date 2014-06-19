package md.varoinform.view.demonstrator;

import md.varoinform.Settings;
import md.varoinform.util.ImageHelper;
import sun.swing.table.DefaultTableCellHeaderRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
* Created with IntelliJ IDEA.
* User: Vladimir Borovic
* Date: 6/17/14
* Time: 12:23 PM
*/
class HeaderRenderer extends DefaultTableCellHeaderRenderer {
    private Set<Integer> filteredColumns = new HashSet<>();
    private Map<Integer, SortOrder> sortedColumns = new HashMap<>();

    public void setFilteredColumns(Set<Integer> filteredColumns) {
        this.filteredColumns = filteredColumns;
    }

    public void setSortedColumns(Map<Integer, SortOrder> sortedColumns) {
        this.sortedColumns = sortedColumns;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        label.setFont(new Font(Settings.Fonts.SANS_SERIF.getName(), Font.BOLD, 14));
        FontMetrics fontMetrics = getFontMetrics(getFont());
        int size = fontMetrics.getHeight();
        Icon icon = getIcon(column, size);
        label.setIcon(icon);
        return label;
    }

    public ImageIcon getIcon(int column, int size) {
        boolean hasIcon = false;
        BufferedImage image = new BufferedImage(size*2, size, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();
        if (filteredColumns.contains(column)) {
            ImageIcon filterIcon = ImageHelper.getScaledImageIcon("/external-resources/icons/filter.png", size, size);
            graphics.drawImage(filterIcon.getImage(), 0, 0, null);
            hasIcon = true;
        }
        SortOrder type = sortedColumns.get(column);
        if (type  != null) {
            String filename = String.format("/external-resources/icons/sort_%s.png", type.toString().toLowerCase());
            ImageIcon sortedIcon = ImageHelper.getScaledImageIcon(filename, size, size);
            graphics.drawImage(sortedIcon.getImage(), size, 0, null);
            hasIcon = true;
        }
        if (hasIcon){
            return new ImageIcon(image);
        }
        return null;
    }
}
