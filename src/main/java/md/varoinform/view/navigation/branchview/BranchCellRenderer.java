package md.varoinform.view.navigation.branchview;

import md.varoinform.util.ResourceBundleHelper;
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

        rendererHelper.setBackground(sel, Color.WHITE);

        if (value instanceof BranchNode){
            BranchNode branchNode = (BranchNode) value;
            String title = branchNode.getTitle();
            if (branchNode.isRoot()) {
                title = ResourceBundleHelper.getString(title, title);
            }
            return RendererHelper.getPanel(title);
        }

        return this;
    }

}
