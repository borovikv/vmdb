package md.varoinform.view.dialogs.registration;

import md.varoinform.Settings;
import md.varoinform.util.ResourceBundleHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/17/13
 * Time: 5:58 PM
 */

public class RegisterByPhonePanel extends CardPanel {
    private static final String REG_CODE_KEY =  "registrationCode";
    private final JLabel registrationTextLabel;

    private final String idDB;
    private final JLabel codeLabel;

    private FormattedTextField passwordField;

    private RegistrationType type;
    private static final String REGISTRATION_TYPE_KEY = "registration";
    private final JLabel typeLabel;
    private final JRadioButton phoneButton;
    private final JRadioButton internetButton;


    public RegisterByPhonePanel() {
        super("register_by_phone");
        type = RegistrationType.INTERNET;

        typeLabel = new JLabel();
        internetButton = new JRadioButton();
        internetButton.setSelected(true);
        internetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type = RegistrationType.INTERNET;
                setEnableByPhoneComponents(false);
            }
        });
        phoneButton = new JRadioButton();
        phoneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type = RegistrationType.PHONE;
                setEnableByPhoneComponents(true);
            }
        });
        ButtonGroup group = new ButtonGroup();
        group.add(internetButton);
        group.add(phoneButton);

        idDB = ResourceBundleHelper.getStringFromBundle(Settings.getConfigBundleKey(), "id");
        codeLabel = new JLabel();
        codeLabel.setText(idDB);

        passwordField = new FormattedTextField(8);
        registrationTextLabel = new JLabel();
        setEnableByPhoneComponents(false);

        setLayout(createLayout());
    }

    public void setEnableByPhoneComponents(boolean enabled) {
        passwordField.setEnabled(enabled);
        label.setEnabled(enabled);
        codeLabel.setEnabled(enabled);
        registrationTextLabel.setEnabled(enabled);
    }

    private GroupLayout createLayout(){
        GroupLayout layout = new GroupLayout(this);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createParallelGroup()
                        .addComponent(typeLabel)
                        .addComponent(internetButton)
                        .addComponent(phoneButton)
                        .addComponent(label)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(registrationTextLabel)
                                .addComponent(codeLabel))
                        .addComponent(passwordField, 0, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                        .addComponent(typeLabel)
                        .addComponent(internetButton)
                        .addComponent(phoneButton)
                        .addComponent(label, 0, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup()
                                .addComponent(registrationTextLabel)
                                .addComponent(codeLabel))
                        .addComponent(passwordField, 0, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
        );
        return layout;
    }

    @Override
    public boolean isInputValid() {
        return internetButton.isSelected() || passwordField.isValueValid();
    }

    @Override
    protected void updateDisplay() {
        super.updateDisplay();
        registrationTextLabel.setText(ResourceBundleHelper.getString(language, REG_CODE_KEY, "registration code"));
        internetButton.setText(ResourceBundleHelper.getString(language, RegistrationType.INTERNET.getTitle(), "internet"));
        phoneButton.setText(ResourceBundleHelper.getString(language, RegistrationType.PHONE.getTitle(), "phone"));
        typeLabel.setText(ResourceBundleHelper.getString(language, REGISTRATION_TYPE_KEY, "register by"));
    }

    public String getPassword() {
        return (String) passwordField.getValue();
    }

    public void setDocumentListener(final JButton nextButton) {
        // if registration type is internet or input valid enable button
        passwordField.addDocumentListener(new DocumentValidListener(passwordField, nextButton));
        phoneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextButton.setEnabled(false);
            }
        });
    }

    public RegistrationType getType() {
        return type;
    }
}
