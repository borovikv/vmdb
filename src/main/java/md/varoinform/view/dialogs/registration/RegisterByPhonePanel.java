package md.varoinform.view.dialogs.registration;

import md.varoinform.Settings;
import md.varoinform.util.ResourceBundleHelper;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/17/13
 * Time: 5:58 PM
 */

public class RegisterByPhonePanel extends CardPanel {
    private static final String REG_CODE_KEY = "registration_code";
    private final JLabel registrationTextLabel;

    private final String idDB;
    private final JLabel codeLabel;

    private FormattedTextField passwordField;

    public RegisterByPhonePanel() {
        super("register_by_phone");
        idDB = ResourceBundleHelper.getStringFromBundle(Settings.getConfigBundleKey(), "id");
        codeLabel = new JLabel();
        codeLabel.setText(idDB);

        passwordField = new FormattedTextField(8);
        registrationTextLabel = new JLabel();

        setLayout(createLayout());
    }

    private GroupLayout createLayout(){
        GroupLayout layout = new GroupLayout(this);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createParallelGroup()
                        .addComponent(label)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(registrationTextLabel)
                                .addComponent(codeLabel))
                        .addComponent(passwordField, 0, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
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
        return passwordField.isValueValid();
    }

    @Override
    protected void updateDisplay() {
        super.updateDisplay();
        registrationTextLabel.setText(ResourceBundleHelper.getString(language, REG_CODE_KEY, "registration code"));
    }

    public String getPassword() {
        return (String) passwordField.getValue();
    }

    public void setDocumentListener(final JButton nextButton) {
        // if registration type is internet or input valid enable button
        passwordField.addDocumentListener(new DocumentValidListener(passwordField, nextButton));
    }

}
