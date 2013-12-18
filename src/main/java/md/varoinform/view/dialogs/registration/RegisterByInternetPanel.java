package md.varoinform.view.dialogs.registration;

import javax.swing.*;

public class RegisterByInternetPanel extends CardPanel {
    public static final int INTERNET = 1;
    public static final int PHONE = 2;
    FormattedTextField idDBField = new FormattedTextField(4);
    final JRadioButton phoneButton = new JRadioButton("phone");
    final JRadioButton internetButton = new JRadioButton("internet");

    public RegisterByInternetPanel() {
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(phoneButton);
        buttonGroup.add(internetButton);
        internetButton.setSelected(true);
        setLayout(createLayout());
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
        idDBField.getDocument().addDocumentListener(new MyDocumentListener(idDBField, nextButton));
    }

    @Override
    public boolean isInputValid() {
        return idDBField.isValueValid();
    }

    //ToDo:create real implementation
    @Override
    protected void setLabelText() {

    }

    public int getRegisterType() {
        if (internetButton.isSelected()) return INTERNET;
        if (phoneButton.isSelected()) return PHONE;
        return 0;
    }


}