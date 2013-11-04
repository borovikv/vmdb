package md.varoinform.view;

import md.varoinform.controller.HistoryProxy;
import md.varoinform.controller.SearchProxy;
import md.varoinform.util.ImageHelper;

import javax.swing.*;
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
        try {
            /*for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                System.out.println(info.getName());
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    //break;
                }
            }*/
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception exc) {
            System.err.println("Error loading L&F: " + exc);
        }

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));


        NavigationPanel navigationPanel = new NavigationPanel();
        ListPanel listPanel = new ListPanel();
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navigationPanel, listPanel);
        splitPane.setContinuousLayout(true);

        Toolbar toolbar = new Toolbar(listPanel);

        StatusBar statusBar = new StatusBar();

        mainPanel.add(toolbar, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(statusBar, BorderLayout.SOUTH);

        setContentPane(mainPanel);



    }
}
