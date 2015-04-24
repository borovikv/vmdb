package md.varoinform.view.dialogs.registration;

import md.varoinform.controller.DefaultLanguages;
import md.varoinform.util.ResourceBundleHelper;

import javax.swing.*;

public class RegisterByInternetPanel extends CardPanel {
    public static final int INTERNET = 1;
    public static final int PHONE = 2;
    protected DefaultLanguages language = DefaultLanguages.EN;
    private final String PHONE_RADIO_KEY = "phone";
    private final String INTERNET_RADIO_KEY = "internet";

    FormattedTextField idDBField = new FormattedTextField(4);
    final JRadioButton phoneButton;
    final JRadioButton internetButton;

    public RegisterByInternetPanel() {
        super("register_by_internet");

        ButtonGroup buttonGroup = new ButtonGroup();
        phoneButton = new JRadioButton();
        buttonGroup.add(phoneButton);

        internetButton = new JRadioButton();
        buttonGroup.add(internetButton);
        internetButton.setSelected(true);

        updateDisplay();
        setLayout(createLayout());
    }

    @Override
    protected void updateDisplay() {
        phoneButton.setText(ResourceBundleHelper.getString(language, PHONE_RADIO_KEY, PHONE_RADIO_KEY));
        internetButton.setText(ResourceBundleHelper.getString(language, INTERNET_RADIO_KEY, INTERNET_RADIO_KEY));
    }

    public GroupLayout createLayout() {
        GroupLayout layout = new GroupLayout(this);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup()
                                        .addComponent(label)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(phoneButton).addComponent(internetButton))
                                        .addComponent(idDBField, 0,  GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        )
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                        .addComponent(label, 0, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(phoneButton).addComponent(internetButton))
                        .addComponent(idDBField, 0, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
        );
        return layout;
    }

    public String getIdDB() {
        return (String) idDBField.getValue();
    }

    public void setDocumentListener(JButton nextButton) {
        // if input valid enable button
        idDBField.addDocumentListener(new DocumentValidListener(idDBField, nextButton));
    }

    @Override
    public boolean isInputValid() {
        return idDBField.isValueValid();
    }

    public int getRegisterType() {
        if (internetButton.isSelected()) return INTERNET;
        if (phoneButton.isSelected()) return PHONE;
        return 0;
    }


}
