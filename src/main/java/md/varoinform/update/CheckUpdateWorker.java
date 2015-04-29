package md.varoinform.update;

import md.varoinform.Settings;
import md.varoinform.controller.cache.BranchCache;
import md.varoinform.controller.cache.Cache;
import md.varoinform.controller.cache.TagCache;
import md.varoinform.model.utils.SessionManager;
import md.varoinform.sequrity.exception.UnregisteredDBExertion;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.view.dialogs.progress.ActivityDialog;

import javax.swing.*;
import java.io.IOException;
import java.util.Date;
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
            return new Updater().checkUpdate();
        } catch (IOException| UnregisteredDBExertion | ExpiredException e) {
            showMessageDialog(e);
            return false;
        }
    }

    @Override
    protected void done() {
        if(hasUpdate()){
            update();
        }
    }

    private Boolean hasUpdate() {
        Boolean hasUpdate = false;
        try {
            hasUpdate = get();
        } catch (InterruptedException | ExecutionException ignored) {
            ignored.printStackTrace();
        }
        return hasUpdate;
    }

    private void update() {
        String key = "updates_exists_message";
        String defaultValue = "Install updates?";
        String message = ResourceBundleHelper.getString(key, defaultValue);
        if (JOptionPane.showConfirmDialog(null, message) == JOptionPane.YES_OPTION){
            Throwable result = ActivityDialog.start(new UpdateWorker(), ResourceBundleHelper.getString("update_progress_message", "wait..."));
            if (result != null) showMessageDialog(result);
        }
    }

    private void showMessageDialog(Throwable e) {
        //e.printStackTrace();
        String key = e.getMessage();
        String message = ResourceBundleHelper.getString(key, key);
        JOptionPane.showMessageDialog(null, message);
    }

    private class UpdateWorker extends SwingWorker<Throwable, Object> {

        private Integer updated;

        @Override
        protected Throwable doInBackground() throws Exception {
            Throwable cause = null;
            try {
                updated = new Updater().update();
                SessionManager.instance.shutdownAll();
                Cache.instance.update();
                BranchCache.instance.update();
                TagCache.instance.update();
            } catch (Throwable e) {
                cause = e;
            }
            return cause;
        }

        @Override
        protected void done() {
            if (updated != null) {
                String key = "updated-message";
                String defaultValue = "%s updated %s enterprises";
                String date = Settings.getDefaultDateFormat().format(new Date());
                String message = String.format(ResourceBundleHelper.getString(key, defaultValue), date, updated);
                JOptionPane.showMessageDialog(null, message);
            }
        }
    }
}
