package md.varoinform;


import md.varoinform.sequrity.PasswordDB;
import md.varoinform.util.PreferencesHelper;
import md.varoinform.view.MainFrame;

import javax.swing.*;

public class App
{
    MainFrame mainFrame = new MainFrame();

    public static void main( String[] args )
    {
        PreferencesHelper helper = new PreferencesHelper();
        helper.remove("password");
        new App().start();
    }

    public void start(){
        PasswordDB passwordDB = new PasswordDB();
        passwordDB.getPassword();
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);

    }
}
