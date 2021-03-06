package md.varoinform.view.demonstrator;

import md.varoinform.Settings;
import md.varoinform.controller.cache.Cache;
import md.varoinform.controller.entityproxy.EnterpriseProxy;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.util.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

import java.io.StringWriter;
import java.nio.file.Paths;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/31/13
 * Time: 11:08 AM
 */
public class EnterpriseView  {

    private static final Template template = getTemplate();

    private static Template getTemplate() {
        try {
            VelocityEngine ve = new VelocityEngine();
            ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
            String path = Paths.get(Settings.getWorkFolder(), "external-resources", "templates").toAbsolutePath().toString();
            ve.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, path);
            ve.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_CACHE, "true");
            ve.init();
            return ve.getTemplate("enterpriseTemplate.vm", "UTF-8");
        } catch (RuntimeException e){
            e.printStackTrace();
            return null;
        }
    }

    public static String getView(Long id) {
        if (template == null) return "";

        StringWriter sw = new StringWriter();
        template.merge(getContext(id), sw);

        return sw.toString();
    }

    private static VelocityContext getContext(Long id) {
        VelocityContext context = new VelocityContext();
        context.put("i18nHelper", ResourceBundleHelper.class);
        context.put("StringUtils", StringUtils.class);

        for (String field : EnterpriseProxy.getFields()) {
            Object value = Cache.instance.getRawValue(id, field);
            context.put(field, value);
        }
        return context;
    }



}
