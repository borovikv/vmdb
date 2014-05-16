package md.varoinform.controller;

import md.varoinform.controller.entityproxy.EnterpriseProxy;
import md.varoinform.model.entities.Email;
import md.varoinform.model.entities.Enterprise;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/28/13
 * Time: 4:00 PM
 */
public class MailProxy {
    private List<Enterprise> enterprises;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

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
        StringBuilder builder = new StringBuilder("mailto:");

        for (Enterprise enterprise : enterprises) {
            EnterpriseProxy proxy = new EnterpriseProxy(enterprise);
            List<Email> emails = proxy.getEmails();
            for (Email email : emails) {
                String s = email.getEmail();
                if (VALID_EMAIL_ADDRESS_REGEX.matcher(s).find()){
                    builder.append(s);
                    builder.append(";");
                }
            }
        }

        return builder.toString();//.replaceAll("\\s*", "");
    }
}
