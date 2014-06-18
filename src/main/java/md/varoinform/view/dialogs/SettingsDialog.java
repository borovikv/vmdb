package md.varoinform.view.dialogs;

import md.varoinform.util.PreferencesHelper;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.view.I18nCheckBox;
import md.varoinform.view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/18/13
 * Time: 2:28 PM
 */
public class SettingsDialog extends JDialog {

    private final I18nCheckBox showTextInButtonBox;
    private JComboBox<UIManager.LookAndFeelInfo> styles;

    private PreferencesHelper helper;

    public SettingsDialog(final MainFrame mainFrame) {
        setSize(400, 450);
        setModal(true);
        setLocationRelativeTo(null);
        setTitle(ResourceBundleHelper.getString("Settings", "Settings"));
        setLayout(new GridLayout(20, 1));

        helper = new PreferencesHelper();

        showTextInButtonBox = new I18nCheckBox("show_text_in_button");
        showTextInButtonBox.setSelected(helper.getShowTextInButton());
        showTextInButtonBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                helper.setShowTextInButton(showTextInButtonBox.isSelected());
                mainFrame.updateDisplay();
            }
        });
        add(showTextInButtonBox);

        UIManager.LookAndFeelInfo[] installedLookAndFeels = UIManager.getInstalledLookAndFeels();
        styles = new JComboBox<>(installedLookAndFeels);
        styles.setRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                String text = ((UIManager.LookAndFeelInfo)value).getName();
                return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
            }
        });
        styles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UIManager.LookAndFeelInfo item = (UIManager.LookAndFeelInfo) styles.getSelectedItem();
                try {
                    UIManager.setLookAndFeel(item.getClassName());
                    SwingUtilities.updateComponentTreeUI(mainFrame);
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
                    e1.printStackTrace();
                }
            }
        });
        add(styles);

        setModal(true);
    }



    public void updateDisplay() {

    }


    public static void showDialog(MainFrame mainFrame) {
        SettingsDialog dialog = new SettingsDialog(mainFrame);
        dialog.setVisible(true);
        dialog.dispose();
    }
}
