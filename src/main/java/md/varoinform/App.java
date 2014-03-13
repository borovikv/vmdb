package md.varoinform;


import md.varoinform.sequrity.PasswordDB;
import md.varoinform.sequrity.StringUtils;
import md.varoinform.util.PreferencesHelper;
import md.varoinform.view.MainFrame;

import javax.swing.*;

public class App
{
    MainFrame mainFrame;

    public static void main( String[] args )
    {
        PreferencesHelper helper = new PreferencesHelper();
        helper.remove("password");
        new App().start();
    }

    public void start(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                /*
                PasswordDB passwordDB = new PasswordDB();
                passwordDB.getPassword();
                */
                mainFrame = new MainFrame();
                mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                mainFrame.setVisible(true);
            }

        });
    }
}
