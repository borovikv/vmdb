package md.varoinform.view;

import md.varoinform.Settings;
import md.varoinform.controller.MailProxy;
import md.varoinform.model.dao.DAOTag;
import md.varoinform.model.dao.EnterpriseDao;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.entities.Tag;
import md.varoinform.model.search.SearchEngine;
import md.varoinform.util.ImageHelper;
import md.varoinform.util.ObservableEvent;
import md.varoinform.util.Observer;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.view.branchview.BranchTree;
import md.varoinform.view.demonstrator.DemonstratorPanel;
import md.varoinform.view.dialogs.ExportDialog;
import md.varoinform.view.dialogs.PrintDialog;
import md.varoinform.view.dialogs.SettingsDialog;
import md.varoinform.view.dialogs.TagDialog;
import md.varoinform.view.tags.TagPanel;

import javax.swing.*;
import javax.swing.event.*;
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
public class MainFrame extends JFrame implements Observer {
    private final SearchEngine searchEngine = new SearchEngine();
    private final JTabbedPane navigationPane;
    private final TagPanel tagPanel = new TagPanel();
    private final History history = new History();
    private final BranchPanel branchPanel = new BranchPanel();
    private final OutputLabel resultLabel = new OutputLabel();
    private final JButton exportButton = new ToolbarButton("/external-resources/icons/export.png");
    private final JButton mailButton = new ToolbarButton("/external-resources/icons/mail.png");
    private final JButton settingsButton = new ToolbarButton("/external-resources/icons/settings.png");
    private final JButton tagButton = new ToolbarButton("/external-resources/icons/star.png");
    private final JButton printButton = new ToolbarButton("/external-resources/icons/print.png");
    private final SearchField searchField = new SearchField();
    private final DemonstratorPanel demonstrator = new DemonstratorPanel();
    private final SettingsDialog settingsDialog;
    private final PrintDialog printDialog;
    private final ExportDialog exportDialog;
    private final TagListener tagListener = new TagListener();
    private final TagDialog tagDialog;

    //------------------------------------------------------------------------------------------------------------------
    public MainFrame() throws HeadlessException {
        settingsDialog = new SettingsDialog(this);
        printDialog = new PrintDialog(this, demonstrator);
        exportDialog = new ExportDialog(this, demonstrator);
        tagDialog = new TagDialog(this, demonstrator);

        tagDialog.addObserver(tagPanel);
        branchPanel.addObserver(this);
        history.addObserver(this);
        demonstrator.addObserver(tagListener);
        demonstrator.addObserver(tagDialog);
        settingsDialog.addObserver(demonstrator);
        tagPanel.addObserver(this);
        tagPanel.addObserver(tagListener);

        setTitle("Varo-Inform Database");
        JFrame.setDefaultLookAndFeelDecorated(true);
        ImageIcon image = ImageHelper.getImageIcon("/external-resources/icons/V.png");
        setIconImage(image.getImage());
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(400, 300));
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        JToolBar toolbar = createToolBar();
        mainPanel.add(toolbar, BorderLayout.NORTH);

        navigationPane = createNavigationPane();
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navigationPane, demonstrator);
        splitPane.setContinuousLayout(true);
        splitPane.setDividerLocation(300);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        JPanel statusBar = createStatusBar();
        mainPanel.add(statusBar, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        updateDisplay();

        List<Enterprise> enterprises = EnterpriseDao.getEnterprises();
        demonstrator.showResults(enterprises);
        resultLabel.setResultCount(enterprises.size());
    }

    private JToolBar createToolBar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);

        toolbar.add(history.getHomeButton());
        toolbar.addSeparator();


        toolbar.add(history.getBackButton());

        toolbar.add(history.getForwardButton());
        toolbar.addSeparator();

        searchField.setFont(Settings.getDefaultFont("SANS_SERIF"));
        searchField.addActionListener(new SearchAction());
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
        return toolbar;
    }


    private JTabbedPane createNavigationPane() {
        JTabbedPane navigationPane = new JTabbedPane(JTabbedPane.TOP);
        navigationPane.addTab("", ImageHelper.getScaledImageIcon("/external-resources/icons/tree.png", 24, 24), branchPanel);
        navigationPane.addTab("", ImageHelper.getScaledImageIcon("/external-resources/icons/star.png", 24, 24), tagPanel);
        navigationPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                tagListener.enableDeleting(false);

                JTabbedPane source = (JTabbedPane) e.getSource();
                NavigationPaneList list = (NavigationPaneList) source.getSelectedComponent();
                list.updateSelection();
            }
        });

        return navigationPane;
    }

    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel();
        statusBar.setLayout(new BorderLayout());
        statusBar.add(resultLabel, BorderLayout.WEST);

        LanguageComboBox languageCombo = new LanguageComboBox();
        languageCombo.addObserver(this);
        statusBar.add(languageCombo, BorderLayout.EAST);

        return statusBar;
    }

    private void updateDisplay() {
        mailButton.setToolTipText(ResourceBundleHelper.getString("mail"));
        exportButton.setToolTipText(ResourceBundleHelper.getString("export"));
        printButton.setToolTipText(ResourceBundleHelper.getString("print"));
        tagButton.setToolTipText(ResourceBundleHelper.getString("tag"));
        settingsButton.setToolTipText(ResourceBundleHelper.getString("dialogs"));
        navigationPane.setTitleAt(0, ResourceBundleHelper.getString("treebranch"));
        navigationPane.setTitleAt(1, ResourceBundleHelper.getString("selected"));
        resultLabel.setMessageText(ResourceBundleHelper.getString("result"));
        branchPanel.updateRoot();
        demonstrator.updateDisplay();
        printDialog.updateDisplay();
        exportDialog.updateDisplay();
        settingsDialog.updateDisplay();
        tagDialog.updateDisplay();
        history.updateDisplay();
        searchField.updateDisplay();
    }


    public void performHistoryMove(Object obj) {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if (BranchTree.isTreePath(obj)) {
            branchPanel.select(obj);

        } else if (obj instanceof String) {
            String text = (String) obj;
            searchField.setText(text);
            searchText(text);
            branchPanel.clearSelection();

        } else {
            List<Enterprise> enterprises = EnterpriseDao.getEnterprises();
            demonstrator.showResults(enterprises);
            branchPanel.clearSelection();
        }
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    private void searchText(String value) {
        if (value == null) return;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        List<Enterprise> enterprises = searchEngine.search(value, null);
        showResults(enterprises);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    }

    private void showResults(List<Enterprise> enterprises) {
        demonstrator.showResults(enterprises);
        resultLabel.setResultCount(enterprises.size());
    }

    @Override
    public void update(ObservableEvent event) {
        switch (event.getType()){
            case ObservableEvent.BRANCH_SELECTED:
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                List<Long> allChildren = branchPanel.getNodes();
                List<Enterprise> enterprises = EnterpriseDao.getEnterprisesByBranchId(allChildren);
                showResults(enterprises);
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                break;

            case ObservableEvent.BRANCH_SELECTED_BY_USER:
                history.appendHistory(branchPanel.getSelectionPath());
                break;

            case ObservableEvent.FORWARD:
            case ObservableEvent.BACK:
            case ObservableEvent.HOME:
                performHistoryMove(event.getValue());
                break;
            case ObservableEvent.TAG_SELECTED:
                Tag tag = tagPanel.getSelectedTag();
                showResults(new ArrayList<>(tag.getEnterprises()));
                break;
            case ObservableEvent.LANGUAGE_CHANGED:
                updateDisplay();
                break;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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


    private class SearchAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String value = e.getActionCommand();
            searchText(value);
            history.appendHistory(value);
            branchPanel.clearSelection();
            tagPanel.clearSelection();
        }

    }


    private class TagListener implements Observer {
        private boolean enableDeleting = false;
        @Override
        public void update(ObservableEvent event) {
            switch (event.getType()){
                case ObservableEvent.TAG_SELECTED:
                    enableDeleting = true;
                    break;
                case ObservableEvent.DELETE:
                    onTagDeleted();
                    break;
            }
        }

        private void onTagDeleted() {
            if (!enableDeleting) return;
            boolean tagExist = removeEnterprisesFromTag();
            tagPanel.update(new ObservableEvent(ObservableEvent.TAGS_CHANGED, !tagExist));
            updateDemonstrator();
        }

        private boolean removeEnterprisesFromTag() {
            DAOTag daoTag = new DAOTag();
            return daoTag.removeTag(tagPanel.getCurrentTagTitle(), demonstrator.getSelected());
        }


        private void updateDemonstrator() {
            Tag selectedTag = tagPanel.getSelectedTag();
            if (selectedTag != null){
                showResults(new ArrayList<>(selectedTag.getEnterprises()));
            } else {
                showResults(new ArrayList<Enterprise>());
            }
        }

        public void enableDeleting(boolean enableDeleting) {
            this.enableDeleting = enableDeleting;
        }
    }
}
