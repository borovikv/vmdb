package md.varoinform.view.dialogs.registration;

import javax.swing.*;

public class RegisterByInternetPanel extends CardPanel {
    FormattedTextField idDBField = new FormattedTextField(4);

    public RegisterByInternetPanel() {
        super("register_by_internet");
        updateDisplay();
        setLayout(createLayout());
    }

    @Override
    protected void updateDisplay() {
        super.updateDisplay();
    }

    public GroupLayout createLayout() {
        GroupLayout layout = new GroupLayout(this);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup()
                                        .addComponent(label)
                                        .addGroup(layout.createSequentialGroup())
                                        .addComponent(idDBField, 0,  GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        )
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                        .addComponent(label, 0, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE))
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
}
