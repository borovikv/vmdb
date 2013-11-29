package md.varoinform.view;

import md.varoinform.controller.HistoryProxy;
import md.varoinform.controller.LanguageProxy;
import md.varoinform.controller.MailProxy;
import md.varoinform.model.dao.EnterpriseDao;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.entities.Language;
import md.varoinform.model.search.SearchEngine;
import md.varoinform.util.ImageHelper;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.view.branchview.BranchTree;
import md.varoinform.view.branchview.BranchTreeNode;
import md.varoinform.view.demonstrator.DemonstratorImpl;
import md.varoinform.view.dialogs.ExportDialog;
import md.varoinform.view.dialogs.PrintDialog;
import md.varoinform.view.dialogs.SettingsDialog;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/30/13
 * Time: 9:15 AM
 */
public class MainFrame extends JFrame{

    private final JTabbedPane navigationPane = new JTabbedPane(JTabbedPane.TOP);
    private BranchTree branchTree = new BranchTree();
    private final LanguageProxy languageProxy = LanguageProxy.getInstance();
    private OutputLabel resultLabel = new OutputLabel();
    private JButton homeButton = new ToolbarButton("/icons/home.png");
    private JButton backButton = new ToolbarButton("/icons/arrow_left2.png", false);
    private JButton forwardButton = new ToolbarButton("/icons/arrow_right2.png", false);
    private JButton printButton = new ToolbarButton("/icons/print.png");
    private JButton exportButton = new ToolbarButton("/icons/export.png");
    private JButton mailButton = new ToolbarButton("/icons/mail.png");
    private JButton settingsButton = new ToolbarButton("/icons/settings.png");
    private JTextField searchField = new JTextField();
    private JComboBox<Language> languageCombo;
    private final HistoryProxy historyProxy = new HistoryProxy();
    private DemonstratorImpl demonstratorImpl = new DemonstratorImpl();
    private SearchEngine searchEngine = new SearchEngine();
    private boolean historyChanged = false;
    private final SettingsDialog settingsDialog;
    private final PrintDialog printDialog;
    private final ExportDialog exportDialog;

    // back
    private final AbstractAction backAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object obj = historyProxy.back();
            performHistoryMove(obj);
            backButton.setEnabled(historyProxy.hasBack());
            forwardButton.setEnabled(true);
        }
    };
    private final JSplitPane splitPane;

    private void performHistoryMove(Object obj) {
        if ( BranchTree.isTreePath(obj) ){
            historyChanged = true;
            branchTree.setSelectionPath((TreePath)obj);
            historyChanged = false;
        } else if ( obj instanceof String ){
            String text = (String) obj;
            searchField.setText(text);
            searchText(text);
            branchTree.clearSelection();
        } else {
            demonstratorImpl.clear();
        }
    }

    // forward
    private final AbstractAction forwardAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object obj = historyProxy.forward();
            performHistoryMove(obj);
            forwardButton.setEnabled(historyProxy.hasForward());
            backButton.setEnabled(true);
        }
    };

    // home
    private final AbstractAction homeAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            historyProxy.home();
            demonstratorImpl.clear();
            branchTree.clearSelection();
        }
    };

    // search
    private final ActionListener searchListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String value = e.getActionCommand();
            searchText(value);
            appendHistory(value);
        }
    };

    private void appendHistory(Object value) {
        historyProxy.append(value);
        forwardButton.setEnabled(false);
        backButton.setEnabled(true);
    }

    private void searchText(String value) {
        if (value == null) return;

        List<Enterprise> enterprises = searchEngine.search(value);
        demonstratorImpl.showResults(enterprises);
        resultLabel.setResultCount(enterprises.size());
    }

    // branch tree selection
    private final TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {
        @Override
        public void valueChanged(TreeSelectionEvent e) {
            BranchTree tree = (BranchTree) e.getSource();
            if ( !tree.isNeedToProcess() ) return;

            BranchTreeNode node = (BranchTreeNode) tree.getLastSelectedPathComponent();
            if (node == null) return;

            List<Long> allChildren = node.getAllChildren();
            List<Enterprise> enterprises = EnterpriseDao.getEnterprisesByBranchId(allChildren);
            demonstratorImpl.showResults(enterprises);
            resultLabel.setResultCount(enterprises.size());
            if (!historyChanged) {
                TreePath selectionPath = tree.getSelectionPath();
                appendHistory(selectionPath);
            }
        }
    };

    // language
    private AbstractAction languageChangedAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Language newLanguage = (Language) languageCombo.getSelectedItem();
            languageProxy.setCurrentLanguage(newLanguage);
            updateDisplay();
        }
    };

    //------------------------------------------------------------------------------------------------------------------
    public MainFrame() throws HeadlessException {
        settingsDialog = new SettingsDialog(this);
        printDialog = new PrintDialog(this, demonstratorImpl);
        exportDialog = new ExportDialog(this, demonstratorImpl);

        setTitle("Varo-Inform Database");
        JFrame.setDefaultLookAndFeelDecorated(true);
        ImageIcon image = ImageHelper.getImageIcon("/icons/V.png");
        setIconImage(image.getImage());
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(400, 300));
        setLocationRelativeTo(null);

        //--------------------------------------------------------------------------------------------------------------
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);

        homeButton.addActionListener(homeAction);
        toolbar.add(homeButton);
        toolbar.addSeparator();

        backButton.addActionListener(backAction);
        toolbar.add(backButton);

        forwardButton.addActionListener(forwardAction);
        toolbar.add(forwardButton);
        toolbar.addSeparator();

        searchField.setFont(new Font(Font.SERIF, Font.PLAIN, 18));
        searchField.addActionListener(searchListener);
        toolbar.add(searchField);

        toolbar.addSeparator();

        toolbar.add(exportButton);
        exportButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                  exportDialog.setVisible(true);
            }
        });
        toolbar.addSeparator();
        toolbar.add(mailButton);
        mailButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Enterprise> enterprises = demonstratorImpl.getSelected();
                if (enterprises.size() == 0) {
                    enterprises = demonstratorImpl.getALL();
                }
                MailProxy mailProxy = new MailProxy(enterprises);
                mailProxy.mail();
            }
        });
        toolbar.addSeparator();

        printButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printDialog.setVisible(true);
            }
        });
        toolbar.add(printButton);
        toolbar.addSeparator();

        settingsButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingsDialog.setVisible(true);
            }
        });
        toolbar.add(settingsButton);

        //--------------------------------------------------------------------------------------------------------------
        branchTree.addTreeSelectionListener(treeSelectionListener);
        navigationPane.addTab("", ImageHelper.getScaledImageIcon("/icons/tree.png", 24, 24), new JScrollPane(branchTree));
        navigationPane.addTab("", ImageHelper.getScaledImageIcon("/icons/star.png", 24, 24), new JPanel());

        //--------------------------------------------------------------------------------------------------------------
        //noinspection unchecked
        languageCombo = new JComboBox(languageProxy.getLanguages().toArray());
        languageCombo.addActionListener(languageChangedAction);

        JPanel statusBar = new JPanel();
        statusBar.setLayout(new BorderLayout());
        statusBar.add(resultLabel, BorderLayout.WEST);
        statusBar.add(languageCombo, BorderLayout.EAST);

        //--------------------------------------------------------------------------------------------------------------
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        mainPanel.add(toolbar, BorderLayout.NORTH);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navigationPane, demonstratorImpl);
        splitPane.setContinuousLayout(true);
        settingsDialog.addObserver(demonstratorImpl);

        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(statusBar, BorderLayout.SOUTH);
        setContentPane(mainPanel);
        updateDisplay();
        sizingPerform();

    }

    private void updateDisplay() {
        homeButton.setToolTipText(ResourceBundleHelper.getString("home"));
        backButton.setToolTipText(ResourceBundleHelper.getString("back"));
        forwardButton.setToolTipText(ResourceBundleHelper.getString("forward"));
        mailButton.setToolTipText(ResourceBundleHelper.getString("mail"));
        exportButton.setToolTipText(ResourceBundleHelper.getString("export"));
        printButton.setToolTipText(ResourceBundleHelper.getString("print"));
        settingsButton.setToolTipText(ResourceBundleHelper.getString("dialogs"));
        navigationPane.setTitleAt(0, ResourceBundleHelper.getString("treebranch"));
        navigationPane.setTitleAt(1, ResourceBundleHelper.getString("selected"));
        resultLabel.setMessageText(ResourceBundleHelper.getString("result"));
        branchTree.updateRoot();
        demonstratorImpl.updateDisplay();
    }

    private void sizingPerform(){
        splitPane.setDividerLocation(300);
    }
}
