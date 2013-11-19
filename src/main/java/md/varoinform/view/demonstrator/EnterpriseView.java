package md.varoinform.view.demonstrator;

import md.varoinform.controller.comparators.ViewPartPriorityComparator;
import md.varoinform.controller.entityproxy.EnterpriseProxy;
import md.varoinform.model.entities.*;
import md.varoinform.util.ResourceBundleHelper;

import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/31/13
 * Time: 11:08 AM
 */
public class EnterpriseView  {

    public static String getView(Enterprise enterprise) {
        EnterpriseProxy enterpriseProxy = new EnterpriseProxy(enterprise);
        return  getTable(enterpriseProxy);
    }

    public static String getTable(EnterpriseProxy enterpriseProxy) {
        List<String> viewParts = EnterpriseProxy.getViewPartNames();
        Collections.sort(viewParts, new ViewPartPriorityComparator());
        System.out.println(viewParts);

        StringBuilder builder = new StringBuilder("<html><body><h1>");
        builder.append(enterpriseProxy.getTitle());
        builder.append("</h1><table>");

        for (String viewPart : viewParts) {
            builder.append("<tr><th>");
            String label = ResourceBundleHelper.getString(viewPart, viewPart);
            builder.append(label);
            builder.append("</th><td>");

            String result = getStringOrNA(enterpriseProxy.get(viewPart));
            builder.append(result);
            builder.append("</td></tr>");
        }
        builder.append("</table></body></html>");

        return builder.toString();
    }

    private static String getStringOrNA(String s){
        return s == null || s.isEmpty() ?  ResourceBundleHelper.getString("null") : s;
    }

}
