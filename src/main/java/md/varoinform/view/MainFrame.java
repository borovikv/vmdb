package md.varoinform.view;

import md.varoinform.Settings;
import md.varoinform.controller.Cache;
import md.varoinform.controller.Holder;
import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.dao.EnterpriseDao;
import md.varoinform.model.entities.Node;
import md.varoinform.model.entities.Tag;
import md.varoinform.model.util.SessionManager;
import md.varoinform.update.CheckUpdateWorker;
import md.varoinform.util.ImageHelper;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.util.observer.ObservableEvent;
import md.varoinform.util.observer.Observer;
import md.varoinform.view.demonstrator.DemonstratorPanel;
import md.varoinform.view.dialogs.AboutDialog;
import md.varoinform.view.dialogs.ProxySettingsDialog;
import md.varoinform.view.dialogs.ShowTextButton;
import md.varoinform.view.dialogs.export.ExportDialog;
import md.varoinform.view.dialogs.print.PrintDialog;
import md.varoinform.view.dialogs.progress.ActivityDialog;
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private final BackButton backButton = new BackButton();
    private final ForwardButton forwardButton = new ForwardButton();
    private boolean enableDeleting = false;

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
                JPopupMenu menu = getSettingsMenu();
                menu.show(settingsButton, settingsButton.getX(), settingsButton.getY() + settingsButton.getHeight());
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
            public void perform(List<Long> enterprises) {
                tagPanel.clearSelection();
                branchPanel.clearSelection();
                showResults(enterprises);
            }
        });

        // BRANCH_SELECTED
        branchPanel.addObserver(this);
        // DELETE
        demonstrator.addObserver(this);
        // TAG_SELECTED,
        tagPanel.addObserver(this);
        // LANGUAGE_CHANGED
        StatusBar.instance.addObserver(this);
        StatusBar.instance.addObserver(Cache.instance);

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

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ActivityDialog.start(new SwingWorker<Object, Object>() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        try {
                            Cache.instance.shutDown();
                            while (Holder.await()){
                                Thread.sleep(500);
                            }
                        } catch (Exception ignored){}
                        finally {
                            SessionManager.instance.shutdownAll();
                        }

                        return null;
                    }
                }, ResourceBundleHelper.getString("close_window_message", "Wait..."));
            }
        });
        new CheckUpdateWorker().execute();
    }

    public JPopupMenu getSettingsMenu() {
        JPopupMenu menu = new JPopupMenu();
        menu.add(new ShowTextButton(this));
        menu.addSeparator();

        JMenuItem proxyItem = new JMenuItem(ResourceBundleHelper.getString("proxy_settings_title", "Proxy Settings"));
        proxyItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProxySettingsDialog.showDialog();
            }
        });
        menu.add(proxyItem);
        menu.addSeparator();

        JMenuItem helpItem = new JMenuItem(ResourceBundleHelper.getString("help", "Help"));
        helpItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!Desktop.isDesktopSupported()) return;

                try {
                    Path path = Paths.get(Settings.getWorkFolder(), "external-resources", "help", String.format("main_%s.html", LanguageProxy.getCurrentLanguageTitle().substring(0, 2)));
                    URI uri = path.toUri();
                    Desktop.getDesktop().browse(uri);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        });
        menu.add(helpItem);

        JMenuItem aboutItem = new JMenuItem(ResourceBundleHelper.getString("about", "About"));
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AboutDialog.showDialog();
            }
        });
        menu.add(aboutItem);
        return menu;
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
                enableDeleting = false;
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
                List<Long> enterpriseIds = Cache.instance.getEnterpriseIdByNode(node);
                showResults(enterpriseIds);
                break;

            case LANGUAGE_CHANGED:
                updateDisplay();
                break;


            case TAG_SELECTED:
                Tag tag = (Tag) event.getValue();
                enableDeleting = tag != null;
                List<Long> enterprises = new EnterpriseDao().getEnterpriseIdsByTag(tag);
                showResults(enterprises);
                break;

            case DELETE:
                if (enableDeleting && isTagSelected()) {
                    @SuppressWarnings("unchecked")
                    List<Long> eids = (List<Long>) event.getValue();
                    tagPanel.deleteEnterpriseFromTag(eids);
                }
                break;

        }
    }

    void showResults(List<Long> enterprises) {
        demonstrator.showResults(enterprises);
    }

    public boolean isTagSelected() {
        Component component = navigationPane.getSelectedComponent();
        return component instanceof TagPanel && ((TagPanel) component).getSelectedTag() != null;
    }

}
