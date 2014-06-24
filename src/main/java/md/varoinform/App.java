package md.varoinform;


import md.varoinform.model.util.SessionManager;
import md.varoinform.view.MainFrame;

import javax.swing.*;

public class App
{
    MainFrame mainFrame;

    public static void main( String[] args )
    {
        new App().start();
    }

    public void start(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //PreferencesHelper helper = new PreferencesHelper();
                //helper.remove("password");
                /*
                PasswordDB passwordDB = new PasswordDB();
                passwordDB.getPassword();
                */

                mainFrame = new MainFrame();
                mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                mainFrame.setVisible(true);
                Runtime.getRuntime().addShutdownHook(new Thread(){
                    @Override
                    public void run() {
                        SessionManager.shutdownAll();
                    }
                });
            }

        });
    }
}
