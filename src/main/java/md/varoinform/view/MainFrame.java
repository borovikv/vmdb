package md.varoinform.view;

import md.varoinform.controller.HistoryProxy;
import md.varoinform.controller.SearchProxy;
import md.varoinform.util.ImageHelper;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/30/13
 * Time: 9:15 AM
 */
public class MainFrame extends JFrame{
    public MainFrame() throws HeadlessException {
        setTitle("Varo-Inform Database");
        JFrame.setDefaultLookAndFeelDecorated(true);
        ImageIcon image = ImageHelper.getImageIcon("/icons/V.png");
        setIconImage(image.getImage());
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(400, 300));
        setLocationRelativeTo(null);

        ListPanel listPanel = new ListPanel();
        Toolbar toolbar = new Toolbar(listPanel);
        NavigationPanel navigationPanel = new NavigationPanel(listPanel);
        navigationPanel.setHistoryProxy(toolbar.getHistoryProxy());
        StatusBar statusBar = new StatusBar();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        mainPanel.add(toolbar, BorderLayout.NORTH);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navigationPanel, listPanel);
        splitPane.setContinuousLayout(true);
        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(statusBar, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }
}
