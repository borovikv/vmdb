package md.varoinform.sequrity;

import md.varoinform.model.dao.DatabaseDao;
import md.varoinform.sequrity.exception.PasswordException;
import md.varoinform.util.PreferencesHelper;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.view.dialogs.registration.RegistrationDialog;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 7/1/14
 * Time: 1:32 PM
 */
public class Guardian {
    public void check(){
        PasswordManager pm = new PasswordManager();
        String uid = new PreferencesHelper().getUID();
        try {
            pm.getDBPassword(uid);
        } catch (PasswordException exception) {
            register(pm, uid);
        }

        DatabaseDao dao = new DatabaseDao();
        if (!dao.checkUID(uid)){
            register(pm, uid);
        }

    }

    public void register(PasswordManager pm, String uid) {
        RegistrationDialog.register();
        try {
            pm.getDBPassword(uid);
        } catch (PasswordException e) {
            String message = ResourceBundleHelper.getString(RegistrationDialog.language, e.getMessage(), e.getMessage());
            JOptionPane.showMessageDialog(null, message);
            System.exit(-1);
        }
    }
}
