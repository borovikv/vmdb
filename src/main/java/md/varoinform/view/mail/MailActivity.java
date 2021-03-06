package md.varoinform.view.mail;

import md.varoinform.controller.cache.Cache;
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
    private final List<Long> enterpriseIds;

    public MailActivity(Desktop desktop, List<Long> enterpriseIds) {
        this.desktop = desktop;
        this.enterpriseIds = enterpriseIds;
    }

    @Override
    protected Void doInBackground() throws Exception {
        if (enterpriseIds.isEmpty()) return null;

        String mailTo = "mailto:";
        StringBuilder builder = new StringBuilder(mailTo);
        for (Long id : enterpriseIds) {

            @SuppressWarnings("unchecked") Iterable<String> emails = (Iterable<String>) Cache.instance.getRawValue(id, "emails");
            for (String email : emails) {

                if (VALID_EMAIL_ADDRESS_REGEX.matcher(email).find()){
                    builder.append(email);
                    builder.append(";");
                }

            }
        }

        String url = builder.toString();
        if (mailTo.equalsIgnoreCase(url)) return null;
        mail(url);

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
