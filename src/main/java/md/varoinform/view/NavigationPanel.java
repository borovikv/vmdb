package md.varoinform.view;

import md.varoinform.util.ImageHelper;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/30/13
 * Time: 10:33 AM
 */
public class NavigationPanel extends JPanel{
    public NavigationPanel() {
        setLayout(new BorderLayout());
        addTabbedPanel();


    }

    private void addTabbedPanel() {
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addTab("Иерархия", ImageHelper.getScaledImageIcon("/icons/tree.png", 24, 24), createDefaultTree());
        tabbedPane.addTab("Избранное", ImageHelper.getScaledImageIcon("/icons/star.png", 24, 24), createDefaultTree());
        add(tabbedPane);
    }

    private JScrollPane createDefaultTree() {
        JTree tree = new JTree();
        tree.setEditable(true);
        //tree.setRootVisible(false);
        return new JScrollPane(tree);
    }
}
