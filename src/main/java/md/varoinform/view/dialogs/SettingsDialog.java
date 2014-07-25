package md.varoinform.view.dialogs;

import md.varoinform.Settings;
import md.varoinform.util.PreferencesHelper;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.view.I18nCheckBox;
import md.varoinform.view.MainFrame;

import javax.swing.*;
import javax.swing.text.DocumentFilter;
import javax.swing.text.InternationalFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/18/13
 * Time: 2:28 PM
 */
//ToDo: Proxy
public class SettingsDialog extends JDialog {

    private final I18nCheckBox showTextInButton;
    private final I18nCheckBox useProxyButton;
    private final JFormattedTextField proxyField;
    private final JFormattedTextField portField;
    private final JTextField loginField;
    private final JTextField passwordField;

    private final Font fieldFont = Settings.getDefaultFont(Settings.Fonts.SANS_SERIF, 14);
    private final JLabel proxyLabel;
    private final JLabel portLabel;
    private final JLabel loginLabel;
    private final JLabel passwordLabel;
    private PreferencesHelper helper;
    private final JPanel proxyPanel;

    public SettingsDialog(final MainFrame mainFrame) {
        setSize(400, 450);
        setModal(true);
        setLocationRelativeTo(null);
        setTitle(ResourceBundleHelper.getString("Settings", "Settings"));
        setLayout(new BorderLayout());

        helper = new PreferencesHelper();

        showTextInButton = new I18nCheckBox("show_text_in_button");
        showTextInButton.setSelected(helper.getShowTextInButton());
        showTextInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                helper.setShowTextInButton(showTextInButton.isSelected());
                mainFrame.updateDisplay();
            }
        });
        add(showTextInButton, BorderLayout.NORTH);

        passwordField = new JPasswordField();
        passwordField.setFont(fieldFont);

        loginField = new JTextField();
        loginField.setFont(fieldFont);

        portField = new JFormattedTextField(new InternationalFormatter(NumberFormat.getIntegerInstance()){
            private IntFilter intFilter = new IntFilter();

            @Override
            protected DocumentFilter getDocumentFilter() {
                return intFilter;
            }
        });
        portField.setFont(fieldFont);


        proxyField = new JFormattedTextField();
        proxyField.setFont(fieldFont);


        useProxyButton = new I18nCheckBox("use_proxy_label");
        boolean useProxy = helper.getUseProxy();
        useProxyButton.setSelected(useProxy);
        useProxyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (useProxyButton.isSelected()) {
                    enableProxyPanel(true);
                    helper.setUseProxy(true);
                } else {
                    helper.setUseProxy(false);
                    enableProxyPanel(false);
                }
            }
        });

        proxyLabel = createLabel("proxy_label");
        portLabel = createLabel("proxy_port_label");
        loginLabel = createLabel("proxy_login_label");
        passwordLabel = createLabel("proxy_password_label");

        proxyPanel = createProxyPanel();
        String title = ResourceBundleHelper.getString("proxy_settings_title");
        proxyPanel.setBorder(BorderFactory.createTitledBorder(title));
        enableProxyPanel(useProxy);
        add(proxyPanel);
    }

    public static void showDialog(MainFrame mainFrame) {
        SettingsDialog dialog = new SettingsDialog(mainFrame);
        dialog.setVisible(true);
        dialog.dispose();
    }

    private void enableProxyPanel(boolean enabled) {
        proxyPanel.setEnabled(enabled);
        proxyField.setEnabled(enabled);
        portField.setEnabled(enabled);
        passwordField.setEnabled(enabled);
        loginField.setEnabled(enabled);
        proxyLabel.setEnabled(enabled);
        portLabel.setEnabled(enabled);
        loginLabel.setEnabled(enabled);
        passwordLabel.setEnabled(enabled);
    }

    private JPanel createProxyPanel() {
        JPanel proxyPanel = new JPanel();
        GroupLayout layout = new GroupLayout(proxyPanel);
        proxyPanel.setLayout(layout);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);

        FontMetrics metrics = getFontMetrics(fieldFont);
        int portFieldWidth = metrics.charWidth('0') * 5 + 10;

        layout.setHorizontalGroup(layout.createParallelGroup()
                        .addComponent(useProxyButton)
                        .addGroup(layout.createSequentialGroup()
                                        .addComponent(proxyLabel)
                                        .addComponent(proxyField)
                                        .addComponent(portLabel)
                                        .addComponent(portField, portFieldWidth, portFieldWidth, portFieldWidth)
                        )
                        .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup()
                                                        .addComponent(loginLabel)
                                                        .addComponent(passwordLabel)
                                        )
                                        .addGroup(layout.createParallelGroup()
                                                        .addComponent(loginField)
                                                        .addComponent(passwordField)
                                        )
                        )
        );

        int height = metrics.getHeight() + 10;
        layout.setVerticalGroup(layout.createSequentialGroup()
                        .addComponent(useProxyButton)
                        .addGroup(layout.createParallelGroup()
                                        .addComponent(proxyLabel)
                                        .addComponent(proxyField, height, height, height)
                                        .addComponent(portLabel)
                                        .addComponent(portField, height, height, height)
                        )
                        .addGroup(layout.createParallelGroup()
                                        .addComponent(loginLabel)
                                        .addComponent(loginField, height, height, height)
                        )
                        .addGroup(layout.createParallelGroup()
                                        .addComponent(passwordLabel)
                                        .addComponent(passwordField, height, height, height)
                        )
        );
        return proxyPanel;
    }

    private JLabel createLabel(String proxy_label) {
        String str = ResourceBundleHelper.getString(proxy_label, proxy_label);
        return new JLabel(str);
    }

    public void updateDisplay() {

    }
}
