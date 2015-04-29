package md.varoinform.view.mail;

import md.varoinform.view.demonstrator.Demonstrator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/28/13
 * Time: 4:00 PM
 */
public class MailAction implements ActionListener {
    private final Demonstrator demonstrator;

    public MailAction(Demonstrator demonstrator) {
        this.demonstrator = demonstrator;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        List<Integer> enterprises = demonstrator.getSelected();
        mail(enterprises);
    }

    public void mail(List<Integer> enterprises){
        Desktop desktop;

        if (Desktop.isDesktopSupported() && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL)){
            MailActivity activity = new MailActivity(desktop, enterprises);
            activity.execute();
        }
    }
}
