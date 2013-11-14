package md.varoinform.view;

import md.varoinform.controller.HistoryProxy;
import md.varoinform.controller.LanguageProxy;
import md.varoinform.controller.enterprisecomparators.DefaultComparator;
import md.varoinform.controller.enterprisecomparators.TitleComparator;
import md.varoinform.model.dao.EnterpriseDao;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.entities.Language;
import md.varoinform.model.search.SearchEngine;
import md.varoinform.util.ImageHelper;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;
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
    private JButton printButton = new ToolbarButton("/icons/print.png", false);
    private JButton exportButton = new ToolbarButton("/icons/export.png", false);
    private JButton mailButton = new ToolbarButton("/icons/mail.png", false);
    private JButton settingsButton = new ToolbarButton("/icons/settings.png", false);
    private JTextField searchField = new JTextField();
    private JComboBox<Comparator<Enterprise>> orderingCombo;
    private JComboBox<Language> languageCombo;
    private final HistoryProxy historyProxy = new HistoryProxy();
    private ListPanel listPanel = new ListPanel();
    private SearchEngine searchEngine = new SearchEngine();
    private List<Comparator<Enterprise>> comparators = new ArrayList<>();
    private boolean historyChanged = false;

    // ordering
    private final ItemListener itemListener = new ItemListener() {
        private List<Enterprise> enterprises;

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.DESELECTED && e.getItem() instanceof DefaultComparator) {
                enterprises = new ArrayList<>(listPanel.getALL());

            } else if (e.getStateChange() == ItemEvent.SELECTED && e.getItem() instanceof DefaultComparator) {
                listPanel.showResults(enterprises);

            } else if (e.getStateChange() == ItemEvent.SELECTED) {
                //noinspection unchecked
                Comparator<Enterprise> comparator = (Comparator<Enterprise>) orderingCombo.getSelectedItem();
                List<Enterprise> enterprises = listPanel.getALL();
                Collections.sort(enterprises, comparator);
                listPanel.showResults(enterprises);

            }
        }
    };

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
            listPanel.clear();
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
            listPanel.clear();
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
        listPanel.showResults(enterprises);
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
            listPanel.showResults(enterprises);
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

        comparators.add(new DefaultComparator());
        comparators.add(new TitleComparator());
        //noinspection unchecked
        orderingCombo = new JComboBox(comparators.toArray());
        orderingCombo.addItemListener(itemListener);
        toolbar.add(orderingCombo);
        toolbar.addSeparator();

        toolbar.add(exportButton);
        toolbar.addSeparator();
        toolbar.add(mailButton);
        toolbar.addSeparator();
        toolbar.add(printButton);
        toolbar.addSeparator();
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

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navigationPane, listPanel);
        splitPane.setContinuousLayout(true);

        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(statusBar, BorderLayout.SOUTH);
        setContentPane(mainPanel);
        updateDisplay();
        sizingPerform();
    }

    private void updateDisplay() {
        Locale locale = new Locale(languageProxy.getCurrentLanguageTitle());
        ResourceBundle bundle = ResourceBundle.getBundle("i18n.Strings", locale);
        homeButton.setToolTipText(bundle.getString("home"));
        backButton.setToolTipText(bundle.getString("back"));
        forwardButton.setToolTipText(bundle.getString("forward"));
        orderingCombo.setToolTipText(bundle.getString("orderBy"));
        mailButton.setToolTipText(bundle.getString("mail"));
        exportButton.setToolTipText(bundle.getString("export"));
        printButton.setToolTipText(bundle.getString("print"));
        settingsButton.setToolTipText(bundle.getString("settings"));
        navigationPane.setTitleAt(0, bundle.getString("treeBranch"));
        navigationPane.setTitleAt(1, bundle.getString("selected"));
        resultLabel.setMessageText(bundle.getString("result"));
        branchTree.updateRoot();
        orderingCombo.updateUI();
        listPanel.updateDisplay();
    }

    private void sizingPerform(){
        splitPane.setDividerLocation(300);
    }
}
