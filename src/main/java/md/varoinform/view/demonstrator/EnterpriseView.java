package md.varoinform.view.demonstrator;

import md.varoinform.Settings;
import md.varoinform.controller.entityproxy.EnterpriseProxy;
import md.varoinform.model.entities.*;
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

    public static String getView(Enterprise enterprise) {
        EnterpriseProxy enterpriseProxy = new EnterpriseProxy(enterprise);
        try {
            return  getTable(enterpriseProxy);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String getTable(EnterpriseProxy enterpriseProxy) throws IOException {
        List<String> viewParts = EnterpriseProxy.getFields();
        Map<String, Object> map = new HashMap<>();
        map.put("css", Paths.get(Settings.getWorkFolder(), "external-resources", "style.css"));
        for (String viewPart : viewParts) {
            Object value = enterpriseProxy.get(viewPart);
            if (value == null
                    || (value instanceof String && ((String) value).isEmpty())
                    || (value instanceof Collection && ((Collection) value).isEmpty())
                    || (value instanceof Map && ((Map) value).isEmpty()))  continue;
            String label = ResourceBundleHelper.getString(viewPart, viewPart);
            map.put(viewPart + "Label", label);
            map.put(viewPart, value);
        }

        Path path = Paths.get(Settings.getWorkFolder(), "external-resources", "templates", "EnterpriseTemplate.html");
        byte[] bytes = Files.readAllBytes(path);
        String template = new String(bytes);
        TemplateRenderer renderer = new TemplateRenderer(template);
        String render = renderer.render(map);
        String regex = "<tr><th>[^<]*</th><td>(<ul></ul>)*</td></tr>";
        return render.replaceAll(regex, "");
    }

}
