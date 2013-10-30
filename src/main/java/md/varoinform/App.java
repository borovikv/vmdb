package md.varoinform;


import md.varoinform.view.MainFrame;

import javax.swing.*;

public class App
{
    public static void main( String[] args )
    {
        MainFrame mainFrame = new MainFrame();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }
}
