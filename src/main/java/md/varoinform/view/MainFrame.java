package md.varoinform.view;

import md.varoinform.controller.HistoryProxy;
import md.varoinform.controller.LanguageProxy;
import md.varoinform.controller.MailProxy;
import md.varoinform.model.dao.DAOTag;
import md.varoinform.model.dao.EnterpriseDao;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.entities.Language;
import md.varoinform.model.entities.Tag;
import md.varoinform.model.search.SearchEngine;
import md.varoinform.util.ImageHelper;
import md.varoinform.util.ObservableEvent;
import md.varoinform.util.Observer;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.view.branchview.BranchTree;
import md.varoinform.view.branchview.BranchTreeNode;
import md.varoinform.view.demonstrator.DemonstratorPanel;
import md.varoinform.view.dialogs.ExportDialog;
import md.varoinform.view.dialogs.PrintDialog;
import md.varoinform.view.dialogs.SettingsDialog;
import md.varoinform.view.dialogs.TagDialog;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/30/13
 * Time: 9:15 AM
 */
public class MainFrame extends JFrame{

    private final JTabbedPane navigationPane = new JTabbedPane(JTabbedPane.TOP);
    private final TagPanel tagPanel = new TagPanel();
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
    private JButton tagButton = new ToolbarButton("/icons/star.png");
    private JTextField searchField = new JTextField();
    private JComboBox<Language> languageCombo;
    private final HistoryProxy historyProxy = new HistoryProxy();
    private DemonstratorPanel demonstrator = new DemonstratorPanel();
    private SearchEngine searchEngine = new SearchEngine();
    private boolean historyChanged = false;
    private final SettingsDialog settingsDialog;
    private final PrintDialog printDialog;
    private final ExportDialog exportDialog;
    private TagListener tagListener = new TagListener();
    private final JSplitPane splitPane;
    private TagDialog tagDialog;

    private class TagListener implements Observer {
        private boolean enableDeleting = false;
        @Override
        public void update(ObservableEvent event) {
            if (!enableDeleting) return;

            DAOTag daoTag = new DAOTag();
            boolean tagDeleted = daoTag.removeTag(tagPanel.getCurrentTag(), demonstrator.getSelected());
            tagPanel.fireTagsChanged();
            if (tagDeleted){
                tagPanel.clearSelection();
            }
            Tag selectedTag = tagPanel.getSelectedTag();
            if (selectedTag != null){
                demonstrator.showResults(new ArrayList<>(selectedTag.getEnterprises()));
            } else {
                demonstrator.showResults(null);
            }

        }

        public void enableDeleting(boolean enableDeleting) {
            this.enableDeleting = enableDeleting;
        }
    }
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

    private void performHistoryMove(Object obj) {
        if ( BranchTree.isTreePath(obj) ){
            historyChanged = true;
            branchTree.setSelectionPath((TreePath)obj);
            historyChanged = false;
        } else if (obj instanceof String) {
            String text = (String) obj;
            searchField.setText(text);
            searchText(text);
            branchTree.clearSelection();
        } else {
            demonstrator.clear();
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
            demonstrator.clear();
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
            branchTree.clearSelection();
            tagPanel.clearSelection();
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
        demonstrator.showResults(enterprises);
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
            demonstrator.showResults(enterprises);
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
        printDialog = new PrintDialog(this, demonstrator);
        exportDialog = new ExportDialog(this, demonstrator);
        tagDialog = new TagDialog(this, demonstrator, tagPanel);

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
        toolbar.add(tagButton);
        tagButton.addActionListener(new ShowDialogAction(tagDialog));
        toolbar.addSeparator();
        toolbar.add(exportButton);
        exportButton.addActionListener(new ShowDialogAction(exportDialog));
        toolbar.addSeparator();
        toolbar.add(mailButton);
        mailButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Enterprise> enterprises = demonstrator.getSelected();
                if (enterprises.size() == 0) {
                    enterprises = demonstrator.getALL();
                }
                MailProxy mailProxy = new MailProxy(enterprises);
                mailProxy.mail();
            }
        });
        toolbar.addSeparator();

        printButton.addActionListener(new ShowDialogAction(printDialog));
        toolbar.add(printButton);
        toolbar.addSeparator();

        settingsButton.addActionListener(new ShowDialogAction(settingsDialog));
        toolbar.add(settingsButton);

        //--------------------------------------------------------------------------------------------------------------
        branchTree.addTreeSelectionListener(treeSelectionListener);
        final JScrollPane branchPane = new JScrollPane(branchTree);
        navigationPane.addTab("", ImageHelper.getScaledImageIcon("/icons/tree.png", 24, 24), branchPane);

        tagPanel.addSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                JList list = (JList) e.getSource();
                Tag tag = (Tag) list.getSelectedValue();
                if (tag == null) return;
                ArrayList<Enterprise> enterprises = new ArrayList<>(tag.getEnterprises());
                demonstrator.showResults(enterprises);
                resultLabel.setResultCount(enterprises.size());
                tagListener.enableDeleting(true);
                tagPanel.setCurrentTag(tag.getTitle());
            }
        });
        navigationPane.addTab("", ImageHelper.getScaledImageIcon("/icons/star.png", 24, 24), tagPanel);

        navigationPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                tagListener.enableDeleting(false);

                JTabbedPane pane = (JTabbedPane) e.getSource();

                if (pane.getSelectedIndex() == pane.indexOfComponent(tagPanel)){
                    tagPanel.updateSelection();
                } else if (pane.getSelectedIndex() == pane.indexOfComponent(branchPane)){
                    branchTree.updateSelection();
                }
            }
        });

        demonstrator.addObserver(tagListener);
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

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navigationPane, demonstrator);
        splitPane.setContinuousLayout(true);
        settingsDialog.addObserver(demonstrator);

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
        tagButton.setToolTipText(ResourceBundleHelper.getString("tag"));
        settingsButton.setToolTipText(ResourceBundleHelper.getString("dialogs"));
        navigationPane.setTitleAt(0, ResourceBundleHelper.getString("treebranch"));
        navigationPane.setTitleAt(1, ResourceBundleHelper.getString("selected"));
        resultLabel.setMessageText(ResourceBundleHelper.getString("result"));
        branchTree.updateRoot();
        demonstrator.updateDisplay();
        printDialog.updateDisplay();
        exportDialog.updateDisplay();
        settingsDialog.updateDisplay();
        tagDialog.updateDisplay();
    }

    private void sizingPerform(){
        splitPane.setDividerLocation(300);
    }

    private class ShowDialogAction extends AbstractAction {
        private JDialog dialog;

        private ShowDialogAction(JDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            dialog.setVisible(true);
        }
    }
}
