package md.varoinform.view.dialogs;

import md.varoinform.Settings;
import md.varoinform.util.PreferencesHelper;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.view.I18nCheckBox;

import javax.swing.*;
import javax.swing.text.DocumentFilter;
import javax.swing.text.InternationalFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 7/28/14
 * Time: 9:42 AM
 */
public class ProxySettingsDialog extends JDialog {
    private final I18nCheckBox useProxyButton;
    private final JTextField proxyField;
    private final JFormattedTextField portField;
    private final JTextField loginField;
    private final JTextField passwordField;

    private final Font fieldFont = Settings.getDefaultFont(Settings.Fonts.SANS_SERIF, 14);
    private final JLabel proxyLabel;
    private final JLabel portLabel;
    private final JLabel loginLabel;
    private final JLabel passwordLabel;
    private final PreferencesHelper helper;
    private final JPanel proxyPanel;

    public ProxySettingsDialog() {
        setSize(400, 450);
        setModal(true);
        setLocationRelativeTo(null);
        setTitle(ResourceBundleHelper.getString("proxy_settings_title", "Proxy Settings"));
        setLayout(new BorderLayout());

        helper = new PreferencesHelper();


        passwordField = new JPasswordField();
        passwordField.setFont(fieldFont);
        passwordField.setText(helper.getProxyPassword());

        loginField = new JTextField();
        loginField.setFont(fieldFont);
        loginField.setText(helper.getProxyUser());

        NumberFormat intFormat = NumberFormat.getIntegerInstance();
        intFormat.setGroupingUsed(false);
        portField = new JFormattedTextField(new InternationalFormatter(intFormat) {
            private IntFilter intFilter = new IntFilter();

            @Override
            protected DocumentFilter getDocumentFilter() {
                return intFilter;
            }

        });
        portField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (portField.getText().trim().isEmpty()){
                    portField.setValue(null);
                }
            }
        });
        portField.setFont(fieldFont);
        portField.setValue(helper.getProxyPort());


        proxyField = new JTextField();
        proxyField.setFont(fieldFont);
        proxyField.setText(helper.getProxyAddress());

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
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                if (useProxyButton.isSelected()) {
                    helper.setProxyAddress(proxyField.getText().trim());
                    helper.setProxyPort(portField.getText().trim());
                    helper.setProxyUser(loginField.getText().trim());
                    helper.setProxyPassword(passwordField.getText());
                }
            }
        });
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

    public static void showDialog() {
        ProxySettingsDialog dialog = new ProxySettingsDialog();
        dialog.setVisible(true);
        dialog.dispose();
    }

    public static void main(String[] args) {
        showDialog();
    }
}
