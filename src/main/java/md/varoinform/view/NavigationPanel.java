package md.varoinform.view;

import md.varoinform.controller.Demonstrator;
import md.varoinform.controller.HistoryProxy;
import md.varoinform.model.dao.BranchDao;
import md.varoinform.model.dao.EnterpriseDao;
import md.varoinform.model.entities.Branch;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.util.ImageHelper;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/30/13
 * Time: 10:33 AM
 */
public class NavigationPanel extends JPanel{
    private Demonstrator demonstrator;
    private HistoryProxy historyProxy;

    public NavigationPanel(Demonstrator demonstrator) {
        this.demonstrator = demonstrator;
        setLayout(new BorderLayout());
        addTabbedPanel();


    }

    private void addTabbedPanel() {
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addTab("Иерархия", ImageHelper.getScaledImageIcon("/icons/tree.png", 24, 24), createBranchTree());
        tabbedPane.addTab("Избранное", ImageHelper.getScaledImageIcon("/icons/star.png", 24, 24), new JPanel());
        add(tabbedPane);
    }

    private JScrollPane createBranchTree() {
        BranchTreeNode root = createRoot();
        JTree tree = new JTree(root);
        tree.setCellRenderer(new BranchCellRenderer());
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                JTree tree = (JTree)e.getSource();
                BranchTreeNode node = (BranchTreeNode)tree.getLastSelectedPathComponent();
                if (node == null) return;
                List<Long> allChildren = node.getAllChildren();
                List<Enterprise> enterprises = EnterpriseDao.getEnterprisesByBranchId(allChildren);
                demonstrator.showResults(enterprises);
                historyProxy.append(enterprises);
            }
        });
        return new JScrollPane(tree);
    }

    private BranchTreeNode createRoot() {
        Branch branchRoot = BranchDao.getRoot();
        BranchTreeNode root = new BranchTreeNode(branchRoot);
        createTree(branchRoot, root);
        return root;
    }

    private void createTree(Branch branchRoot, BranchTreeNode root) {
        for (Branch branch : branchRoot.getChildren()) {
            BranchTreeNode branchTreeNode = new BranchTreeNode(branch);
            root.add(branchTreeNode);
            createTree(branch, branchTreeNode);
        }
    }

    public void setHistoryProxy(HistoryProxy historyProxy) {
        this.historyProxy = historyProxy;
    }
}
