package md.varoinform.sequrity;

import md.varoinform.view.dialogs.registration.RegistrationDialog;

import javax.swing.*;

public class PasswordDB {
    public PasswordDB() {
    }

    public String getPassword() {
        String password = null;
        try {
            password = getDBPassword();

        } catch (PasswordNotExistException exception) {

            RegistrationDialog dialog = new RegistrationDialog();
            dialog.setVisible(true);

            try {
                password = getDBPassword();
            } catch (PasswordNotExistException e) {
                exit(exception);
            }
        }
        return password;
    }

    void exit(Throwable exception) {
        JOptionPane.showMessageDialog(null, getMessage(exception));
        System.exit(1);
    }

    String getDBPassword() throws PasswordNotExistException {
        PasswordManager passwordManager = new PasswordManager();

        try {
            return passwordManager.getDBPassword();

        } catch (PasswordException exception) {
            exit(exception);

        }

        return null;
    }

    String getMessage(Throwable exception) {
        return exception.getMessage();
    }
}