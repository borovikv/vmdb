package md.varoinform.view.branchview;

import md.varoinform.view.RendererHelper;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;


/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/5/13
 * Time: 3:48 PM
 */
public class BranchCellRenderer extends DefaultTreeCellRenderer {


    private final RendererHelper rendererHelper  = new RendererHelper();

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean sel, boolean expanded, boolean leaf, int row,
                                                  boolean hasFocus) {

        rendererHelper.setBackground(sel, tree.getBackground());

        if (value instanceof BranchTreeNode){
            BranchTreeNode branchTreeNode = (BranchTreeNode) value;
            String title = branchTreeNode.getTitle();
            return RendererHelper.getPanel(title);
        }

        return this;
    }

}
