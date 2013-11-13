package md.varoinform.controller.enterprisecomparators;

import md.varoinform.controller.ContactProxy;
import md.varoinform.controller.EntityView;
import md.varoinform.controller.LanguageProxy;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/13/13
 * Time: 2:16 PM
 */
public class ContactView extends EntityView {

    public static String getContactTables(List<ContactProxy> contactProxies){
        StringBuilder sb = new StringBuilder();
        for (ContactProxy contactProxy : contactProxies) {
            sb.append(getAddressTable(contactProxy));
        }
        return sb.toString();
    }

    private static String getAddressTable(ContactProxy proxy) {
        ResourceBundle resourceBundle = getResourceBundle();

        String result = "<tr><th>" + resourceBundle.getString("postalCode") + "</th><td>" +getStringOrNA(proxy.getPostalCode()) + "</td></tr>" +
                        "<tr><th>" + resourceBundle.getString("address") + "</th><td>" + getStringOrNA(proxy.getAddress()) + "</td></tr>" +
                        "<tr><th>" + resourceBundle.getString("phone") + "</th><td>" + getStringOrNA(proxy.getPhones()) + "</td></tr>" +
                        "<tr><th>" + resourceBundle.getString("fax") + "</th><td>" + getStringOrNA(proxy.getFax()) + "</td></tr>" +
                        "<tr><th>" + resourceBundle.getString("email") + "</th><td>" + getStringOrNA(proxy.getEmail()) + "</td></tr>" +
                        "<tr><th>" + resourceBundle.getString("urls") + "</th><td>" + getStringOrNA(proxy.getUrls()) + "</td></tr>";
        return result;
    }

    public static String getCellViewContact(List<ContactProxy> contactProxies){
        StringBuilder sb = new StringBuilder();
        String separator = "";
        for (ContactProxy contactProxy : contactProxies) {
            sb.append(separator);
            sb.append(getSmallContact(contactProxy));
            separator = "<br />";
        }
        return sb.toString();
    }

    private static String getSmallContact(ContactProxy proxy) {
        ResourceBundle resourceBundle = getResourceBundle();

        String result = proxy.getPostalCode() + " " + proxy.getAddress() + "<br />"  +
                proxy.getPhones();

        return result;
    }


}
