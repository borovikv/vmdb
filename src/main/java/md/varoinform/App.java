package md.varoinform;


import md.varoinform.controller.DefaultLanguages;
import md.varoinform.model.search.FullTextSearcher;
import md.varoinform.model.util.SessionManager;
import md.varoinform.sequrity.Guardian;
import md.varoinform.util.PreferencesHelper;
import md.varoinform.util.Profiler;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.view.MainFrame;

import javax.swing.*;

public class App {

    public static void main(String[] args) {
        new App().start();
    }

    public void start() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                Profiler pg = new Profiler("check");
                Guardian guardian = new Guardian();
                try {
                    guardian.check();
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            ResourceBundleHelper.getString(DefaultLanguages.RU, "database_in_use_message", "Database already in use"));
                    return;
                }
                pg.end();

                PreferencesHelper preferences = new PreferencesHelper();
                if(!preferences.getIsIndexed()) {
                    if (FullTextSearcher.createIndex()) {
                        preferences.setIsIndexed(true);
                    }
                }

                Profiler p = new Profiler("create mainframe");
                MainFrame mainFrame = new MainFrame();
                SessionManager.instance.shutdownAll();
                p.end();
                mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                mainFrame.setVisible(true);
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    @Override
                    public void run() {
                        SessionManager.instance.shutdownAll();
                    }
                });
            }

        });
    }
}
