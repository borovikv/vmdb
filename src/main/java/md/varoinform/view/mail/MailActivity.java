package md.varoinform.view.mail;

import md.varoinform.controller.entityproxy.EnterpriseProxy;
import md.varoinform.model.entities.Email;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.view.dialogs.progress.Activity;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 5/19/14
 * Time: 10:45 AM
 */
public class MailActivity extends Activity {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private final Desktop desktop;
    private final List<Enterprise> enterprises;

    public MailActivity(Desktop desktop, List<Enterprise> enterprises) {
        this.desktop = desktop;
        this.enterprises = enterprises;
    }

    @Override
    protected Void doInBackground() throws Exception {
        if (enterprises.isEmpty()) return null;

        String mailTo = "mailto:";
        StringBuilder builder = new StringBuilder(mailTo);
        for (Enterprise enterprise : enterprises) {

            List<Email> emails = new EnterpriseProxy(enterprise).getEmails();
            for (Email email : emails) {

                String s = email.getEmail();
                if (VALID_EMAIL_ADDRESS_REGEX.matcher(s).find()){
                    builder.append(s);
                    builder.append(";");
                }

            }
        }

        String url = builder.toString();
        if (mailTo.equalsIgnoreCase(url)) return null;
        mail(mailTo);

        return null;
    }

    private void mail(String mailTo) {
        try {
            URI uri = new URI(mailTo);
            desktop.mail(uri);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }


}
