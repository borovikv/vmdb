package md.varoinform.controller;

import md.varoinform.controller.entityproxy.EnterpriseProxy;
import md.varoinform.model.entities.Enterprise;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/28/13
 * Time: 4:00 PM
 */
public class MailProxy {
    private List<Enterprise> enterprises;

    public MailProxy(List<Enterprise> enterprises) {
        this.enterprises = enterprises;
    }

    public void mail(){
        Desktop desktop;

        if (Desktop.isDesktopSupported() && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL)){

            mail(desktop);

        }
    }

    private void mail(Desktop desktop) {
        try {
            URI uri = new URI( getMailTo() );
            desktop.mail(uri);

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private String getMailTo() {
        StringBuilder builder = new StringBuilder("mailto:vladimir@varo-inform.com, borovikv.vladimir@gmail.com");

        for (Enterprise enterprise : enterprises) {
            EnterpriseProxy proxy = new EnterpriseProxy(enterprise);
            builder.append(proxy.getEmails());
            builder.append(",");
        }

        return builder.toString().replaceAll("\\s*", "");
    }
}
