package md.varoinform.view.branchview;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.entities.Branch;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.ResourceBundle;


/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/5/13
 * Time: 3:48 PM
 */
public class BranchCellRenderer extends DefaultTreeCellRenderer{
    private static final Color HIGHLIGHT_COLOR;
    static {
        ResourceBundle bundle = ResourceBundle.getBundle("VaroDB");
        HIGHLIGHT_COLOR = (Color)bundle.getObject("highlightColor");
    }
    private static Color background;

    public BranchCellRenderer() {
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean sel, boolean expanded, boolean leaf, int row,
                                                  boolean hasFocus) {
        background = sel ? HIGHLIGHT_COLOR: tree.getBackground();

        if (value instanceof BranchTreeNode){
            BranchTreeNode branchTreeNode = (BranchTreeNode) value;
            return branchPanel(branchTreeNode.getBranch());
        }

        return this;
    }



    public static JPanel branchPanel(Branch branch){
        JPanel panel = new JPanel();
        panel.setBackground(background);
        String title = branch.title(LanguageProxy.getInstance().getCurrentLanguage());

        JLabel label = new JLabel(title);
        panel.add(label);

        return panel;

    }
}
