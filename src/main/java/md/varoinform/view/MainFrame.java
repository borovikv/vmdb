package md.varoinform.view;

import md.varoinform.Settings;
import md.varoinform.controller.Cache;
import md.varoinform.model.dao.DAOTag;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.entities.Node;
import md.varoinform.model.entities.Tag;
import md.varoinform.util.ImageHelper;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.util.observer.ObservableEvent;
import md.varoinform.util.observer.Observer;
import md.varoinform.view.demonstrator.DemonstratorPanel;
import md.varoinform.view.dialogs.SettingsDialog;
import md.varoinform.view.dialogs.export.ExportDialog;
import md.varoinform.view.dialogs.print.PrintDialog;
import md.varoinform.view.historynavigator.BackButton;
import md.varoinform.view.historynavigator.ForwardButton;
import md.varoinform.view.mail.MailAction;
import md.varoinform.view.navigation.HomeButton;
import md.varoinform.view.navigation.branchview.BranchPanel;
import md.varoinform.view.navigation.search.SearchListener;
import md.varoinform.view.navigation.search.SearchPanel;
import md.varoinform.view.navigation.tags.TagPanel;
import md.varoinform.view.status.StatusBar;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/30/13
 * Time: 9:15 AM
 */
public class MainFrame extends JFrame implements Observer {
    private final JTabbedPane navigationPane;
    private final TagPanel tagPanel = new TagPanel("");
    private final BranchPanel branchPanel = new BranchPanel();
    private final ToolbarButton exportButton = new ToolbarButton("/external-resources/icons/export.png", "export", "export");
    private final ToolbarButton mailButton = new ToolbarButton("/external-resources/icons/mail.png", "mail", "mail");
    private final ToolbarButton settingsButton = new ToolbarButton("/external-resources/icons/settings.png", "settings", "settings");
    private final ToolbarButton tagButton = new ToolbarButton("/external-resources/icons/star.png", "tag_add", "tag_add");
    private final ToolbarButton printButton = new ToolbarButton("/external-resources/icons/print.png", "print", "print");
    private final SearchPanel searchPanel = new SearchPanel();
    private final DemonstratorPanel demonstrator = new DemonstratorPanel();
    private final HomeButton homeButton = new HomeButton(demonstrator);
    private final TagListener tagListener = new TagListener();
    private final BackButton backButton = new BackButton();
    private final ForwardButton forwardButton = new ForwardButton();

    //------------------------------------------------------------------------------------------------------------------
    public MainFrame() throws HeadlessException {
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homeButton.home();
                branchPanel.clearSelection();
            }
        });
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ExportDialog.export(demonstrator);
            }
        });
        mailButton.addActionListener(new MailAction(demonstrator));

        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PrintDialog.print(demonstrator);
            }
        });
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SettingsDialog.showDialog(MainFrame.this);
            }
        });
        tagButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tagPanel.addTag(demonstrator.getSelected());
            }
        });
        searchPanel.addSearchAction(new SearchListener() {
            @Override
            public void perform(List<Enterprise> enterprises) {
                tagPanel.clearSelection();
                branchPanel.clearSelection();
                showResults(enterprises);
            }
        });

        branchPanel.addObserver(this);
        demonstrator.addObserver(tagListener);
        tagPanel.addObserver(tagListener);
        StatusBar.instance.addObserver(this);

        setTitle("Varo-Inform DATABASE");
        JFrame.setDefaultLookAndFeelDecorated(true);
        setIconImage(Settings.getMainIcon());
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(400, 600));
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        JPanel toolbar = createToolBar();
        mainPanel.add(toolbar, BorderLayout.NORTH);

        navigationPane = createNavigationPane();
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navigationPane, demonstrator);
        splitPane.setContinuousLayout(true);
        splitPane.setDividerLocation(300);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        mainPanel.add(StatusBar.instance.getStatusBar(), BorderLayout.SOUTH);

        setContentPane(mainPanel);

        updateDisplay();

        homeButton.home();
    }

    private JPanel createToolBar() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder());
        GroupLayout layout = new GroupLayout(panel);
        layout.setAutoCreateGaps(true);
        panel.setLayout(layout);
        int minButtonWidth = ToolbarButton.getMinWith();
        int gapWidth = 15;
        layout.setHorizontalGroup(layout.createSequentialGroup()
                        .addComponent(homeButton, minButtonWidth, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                        .addComponent(backButton, minButtonWidth, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                        .addComponent(forwardButton, minButtonWidth, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                        .addGap(0, gapWidth, gapWidth)
                        .addComponent(searchPanel.searchField, 100, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                        .addComponent(searchPanel.searcherCombo, 0, searchPanel.searcherCombo.getMaxWidth(), GroupLayout.PREFERRED_SIZE)
                        .addComponent(searchPanel.searchButton, minButtonWidth, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                        .addGap(0, gapWidth, gapWidth)
                        .addComponent(tagButton, minButtonWidth, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                        .addComponent(exportButton, minButtonWidth, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                        .addComponent(mailButton, minButtonWidth, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                        .addComponent(printButton, minButtonWidth, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                        .addComponent(settingsButton, minButtonWidth, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
        );
        int height = searchPanel.searchField.height();
        int padding = 5;
        int gapHeight = height + padding*2;
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGap(gapHeight, gapHeight, gapHeight)
                        .addComponent(homeButton, height, height, height)
                        .addComponent(backButton, height, height, height)
                        .addComponent(forwardButton, height, height, height)
                        .addComponent(searchPanel.searchField, height, height, height)
                        .addComponent(searchPanel.searcherCombo, height, height, height)
                        .addComponent(searchPanel.searchButton, height, height, height)
                        .addComponent(tagButton, height, height, height)
                        .addComponent(exportButton, height, height, height)
                        .addComponent(mailButton, height, height, height)
                        .addComponent(printButton, height, height, height)
                        .addComponent(settingsButton, height, height, height)
        );

        return panel;
    }


    private JTabbedPane createNavigationPane() {
        JTabbedPane navigationPane = new JTabbedPane(JTabbedPane.TOP);
        navigationPane.addTab("", ImageHelper.getScaledImageIcon("/external-resources/icons/tree.png", 24, 24), branchPanel);
        navigationPane.addTab("", ImageHelper.getScaledImageIcon("/external-resources/icons/star.png", 24, 24), tagPanel);
        navigationPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                tagListener.enableDeleting(false);
            }
        });

        return navigationPane;
    }



    public void updateDisplay() {
        Field[] declaredFields = MainFrame.class.getDeclaredFields();
        for (Field field : declaredFields) {
            try {
                Method updateDisplay = field.getType().getMethod("updateDisplay");
                Object obj = field.get(this);
                updateDisplay.invoke(obj);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
            }
        }

        navigationPane.setTitleAt(0, ResourceBundleHelper.getString("tree", "Tree"));
        navigationPane.setTitleAt(1, ResourceBundleHelper.getString("tag", "Tag"));

        StatusBar.instance.updateDisplay();
    }

    @Override
    public void update(ObservableEvent event) {
        switch (event.getType()){
            case BRANCH_SELECTED:
                Node node = branchPanel.getNode();
                List<Enterprise> enterprises = Cache.instance.getEnterpriseByNode(node);
                showResults(enterprises);
                break;

            case LANGUAGE_CHANGED:
                updateDisplay();
                break;
        }
    }

    private void showResults(List<Enterprise> enterprises) {
        demonstrator.showResults(enterprises);
    }

    private class TagListener implements Observer {
        private boolean enableDeleting = false;
        @Override
        public void update(ObservableEvent event) {
            switch (event.getType()){
                case TAG_SELECTED:
                    enableDeleting = true;
                    Tag tag = tagPanel.getSelectedTag();
                    if (tag == null) {
                        showResults(null);
                    } else {
                        showResults(new ArrayList<>(tag.getEnterprises()));
                    }
                    break;
                case DELETE:
                    onTagDeleted();
                    break;
                case TAGS_CHANGED:
                    tagPanel.update(new ObservableEvent(ObservableEvent.Type.TAGS_CHANGED));
                    break;
                case CLEAR_DEMONSTRATOR:
                    showResults(null);
            }
        }

        private void onTagDeleted() {
            if (!enableDeleting || !isTagSelected()) return;
            boolean tagExist = removeEnterprisesFromTag();
            tagPanel.update(new ObservableEvent(ObservableEvent.Type.TAGS_CHANGED, !tagExist));
            updateDemonstrator();
        }

        private boolean isTagSelected() {
            Component component = navigationPane.getSelectedComponent();
            if (!(component instanceof TagPanel)) return false;
            Tag tag = ((TagPanel) component).getSelectedTag();
            return tag != null;
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
