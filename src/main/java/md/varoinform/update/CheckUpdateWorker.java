package md.varoinform.update;

import md.varoinform.sequrity.exception.UnregisteredDBExertion;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.view.dialogs.progress.ActivityDialog;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

/**
* Created with IntelliJ IDEA.
* User: Vladimir Borovic
* Date: 7/9/14
* Time: 10:47 AM
*/
public class CheckUpdateWorker extends SwingWorker<Boolean, Void> {

    @Override
    protected Boolean doInBackground() throws Exception {
        try {
            return new Updater().checkUpdate("0");
        } catch (UnregisteredDBExertion | ExpiredException ignored) {
            return false;
        }
    }

    @Override
    protected void done() {
        try {
            if(get()){
                update();
            }
        } catch (InterruptedException | ExecutionException e ) {
            showMessageDialog(e);
        }
    }

    private void update() {
        String key = "";
        String defaultValue = "install updates";
        String message = ResourceBundleHelper.getString(key, defaultValue);
        if (JOptionPane.showConfirmDialog(null, message) == JOptionPane.YES_OPTION){
            Throwable result = ActivityDialog.start(new UpdateWorker(), "progress");
            if (result != null) showMessageDialog(result);
        }
    }

    //ToDo: process message for update fail
    private void showMessageDialog(Throwable e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
        e.printStackTrace();
    }

    private class UpdateWorker extends SwingWorker<Throwable, Object> {
        @Override
        protected Throwable doInBackground() throws Exception {
            try {
                new Updater().update();
            } catch (Throwable e) {
                return e;
            }
            return null;
        }
    }
}
