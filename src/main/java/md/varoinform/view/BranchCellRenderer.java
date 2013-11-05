package md.varoinform.view;

import md.varoinform.model.entities.Branch;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import static md.varoinform.model.dao.BranchProxy.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/5/13
 * Time: 3:48 PM
 */
public class BranchCellRenderer extends DefaultTreeCellRenderer{
    private static final Color HIGHLIGHT_COLOR = new Color(255, 255, 204);
    private static final Color ALTERNATIVE_COLOR = new Color(238, 238, 238);
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean sel, boolean expanded, boolean leaf, int row,
                                                  boolean hasFocus) {
        background = sel ? HIGHLIGHT_COLOR: tree.getBackground();
        //foreground = sel ? Color.WHITE : tree.getForeground();

        if (value instanceof BranchTreeNode){
            BranchTreeNode branchTreeNode = (BranchTreeNode) value;
            return branchPanel(branchTreeNode.getBranch());
        }

        return this;
    }



    public static JPanel branchPanel(Branch branch){
        JPanel panel = new JPanel();
        panel.setBackground(background);
        String title = "";
        try {
            title = branch.getTitles().get(0).getTitle();
        } catch (Exception e){}

        JLabel label = new JLabel(title);
        //label.setForeground(foreground);
        panel.add(label);

        return panel;

    }
    private static Color background;
    private static Color foreground;
}
