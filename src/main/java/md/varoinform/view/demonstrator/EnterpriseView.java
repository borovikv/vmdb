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
        try {
            return  getTable(id);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String getTable(Long id) throws IOException {


        LinkedHashSet<String> address = new LinkedHashSet<>();
        address.add((String) Cache.instance.getValue(id, "country"));
        address.add((String) Cache.instance.getValue(id, "sector"));
        address.add((String) Cache.instance.getValue(id, "town"));
        address.add((String) Cache.instance.getValue(id, "StreetHouseOffice"));

        Map<String, Object> context = new HashMap<>();
        context.put("address", address);
        context.put("addressLabel", ResourceBundleHelper.getString("address", "address"));

        for (String field : EnterpriseProxy.getFields()) {
            Object value = Cache.instance.getValue(id, field);
            if (value == null
                    || (value instanceof String && ((String) value).isEmpty())
                    || (value instanceof Collection && ((Collection) value).isEmpty())
                    || (value instanceof Map && ((Map) value).isEmpty()))  continue;
            String label = ResourceBundleHelper.getString(field, field);
            context.put(field + "Label", label);
            context.put(field, value);
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
