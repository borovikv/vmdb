package md.varoinform.view.dialogs.registration;

import md.varoinform.util.ResourceBundleHelper;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/17/13
 * Time: 5:58 PM
 */

public class RegisterByPhonePanel extends CardPanel {
    private final JLabel registrationCodeLabel = new JLabel();
    private FormattedTextField passwordField = new FormattedTextField(8);

    public RegisterByPhonePanel() {
        setLayout(createLayout());
        setLabelText();
    }

    //ToDo:create real implementation
    @Override
    protected void setLabelText() {

    }

    private GroupLayout createLayout(){
        GroupLayout layout = new GroupLayout(this);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addComponent(label)
                .addComponent(registrationCodeLabel)
                .addComponent(passwordField, 0, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(label, 0, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(registrationCodeLabel)
                .addComponent(passwordField, 0, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
        );
        return layout;
    }

    public void setRegistrationCode(String registrationCode) {
        String text = ResourceBundleHelper.getString("registrationCode", "registration code") + " " + registrationCode;
        registrationCodeLabel.setText(text);
    }

    public boolean isInputValid() {
        return passwordField.isValueValid();
    }

    public String getPassword() {
        return (String) passwordField.getValue();
    }

    public void setDocumentListener(JButton nextButton) {
        passwordField.getDocument().addDocumentListener(new MyDocumentListener(passwordField, nextButton));
    }
}
