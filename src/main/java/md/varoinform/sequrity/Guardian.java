package md.varoinform.sequrity;

import md.varoinform.model.dao.DatabaseDao;
import md.varoinform.sequrity.exception.LockedException;
import md.varoinform.sequrity.exception.PasswordException;
import md.varoinform.sequrity.exception.UnregisteredDBExertion;
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
    public void check() throws Exception {
//        if (databaseAlreadyInUse()) throw new Exception();

        PasswordManager pm = new PasswordManager();
        String uid = new PreferencesHelper().getUID();
        try {
            pm.getDBPassword(uid);
        } catch (PasswordException | UnregisteredDBExertion exception) {
            register(pm, uid);
        } catch (LockedException exception) {
            String exceptionMessage = exception.getMessage();
            String message = ResourceBundleHelper.getStringForUserLocale(exceptionMessage, exceptionMessage);
            JOptionPane.showMessageDialog(null, message);
            System.exit(-1);
        }

        String dbUid = DatabaseDao.getUID();
        if (dbUid == null || !dbUid.equalsIgnoreCase(uid)){
            register(pm, uid);
        }

    }


    public void register(PasswordManager pm, String uid) {
        RegistrationDialog.register();
        try {
            pm.getDBPassword(uid);
        } catch (PasswordException|UnregisteredDBExertion e) {
            String message = ResourceBundleHelper.getString(RegistrationDialog.language, e.getMessage(), e.getMessage());
            JOptionPane.showMessageDialog(null, message);
            System.exit(-1);
        } catch (LockedException e) {
            System.exit(-1);
        }
    }
}
