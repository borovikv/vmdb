package md.varoinform.view.demonstrator;

import md.varoinform.Settings;
import md.varoinform.controller.Cache;
import md.varoinform.controller.entityproxy.EnterpriseProxy;
import md.varoinform.util.ResourceBundleHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/31/13
 * Time: 11:08 AM
 */
public class EnterpriseView  {

    public static String getView(Long id) {
        EnterpriseProxy enterpriseProxy = Cache.instance.getProxy(id);
        try {
            return  getTable(enterpriseProxy);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String getTable(EnterpriseProxy enterpriseProxy) throws IOException {
        List<String> viewParts = EnterpriseProxy.getFields();
        Map<String, Object> context = new HashMap<>();
        LinkedHashSet<String> set = new LinkedHashSet<>();
        set.add(enterpriseProxy.getCountry());
        set.add(enterpriseProxy.getSector());
        set.add(enterpriseProxy.getTown());
        set.addAll(enterpriseProxy.getStreetHouseOffice());
        context.put("address", set);
        context.put("addressLabel", ResourceBundleHelper.getString("address", "address"));

        for (String viewPart : viewParts) {
            Object value = enterpriseProxy.get(viewPart);
            if (value == null
                    || (value instanceof String && ((String) value).isEmpty())
                    || (value instanceof Collection && ((Collection) value).isEmpty())
                    || (value instanceof Map && ((Map) value).isEmpty()))  continue;
            String label = ResourceBundleHelper.getString(viewPart, viewPart);
            context.put(viewPart + "Label", label);
            context.put(viewPart, value);
        }

        String template = getTemplate();
        TemplateRenderer renderer = new TemplateRenderer(template);
        String render = renderer.render(context);
        String regex = "<tr><th>[^<]*</th><td>(<ul></ul>)*</td></tr>";
        return render.replaceAll(regex, "");
    }

    private static String getTemplate() throws IOException {
        Path path = Paths.get(Settings.getWorkFolder(), "external-resources", "templates", "EnterpriseTemplate.html");
        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes);
    }

}
