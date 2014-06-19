package md.varoinform.view.dialogs.print;

import md.varoinform.controller.Cache;
import md.varoinform.controller.entityproxy.EnterpriseProxy;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.entities.Language;
import md.varoinform.util.StringUtils;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vb
 * Date: 11/23/13
 * Time: 5:25 AM
 */
public class Address extends PrintableBase {
    private final BasicStroke dashedStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{10.0f}, 0.0f);

    public Address(List<Enterprise> enterprises, Language language) {
        super(enterprises, language);
        width = (int)(3.0 * inchToPTCoefficient);
        height = (int) (1.5 * inchToPTCoefficient);
    }

    @Override
    protected void drawItem(float x, float y, Graphics2D g2, Enterprise enterprise) {
        Rectangle2D rectangle = new Rectangle2D.Double(x, y, width, height);
        g2.setStroke(dashedStroke);
        g2.draw(rectangle);

        Font f = new Font("SanSerif", Font.PLAIN, 10);
        g2.setFont(f);

        int indent = 15;
        int fontHeight = g2.getFontMetrics().getHeight();
        float xoff = x + indent;
        int lineHeight = fontHeight;
        int maxWriteAreaWidth = (int) rectangle.getWidth() - indent * 2;


        EnterpriseProxy enterpriseProxy = Cache.instance.getProxy(enterprise, language);
        String[] strings = new String[]{
                enterpriseProxy.getTitle(),
                StringUtils.valueOf(enterpriseProxy.getStreetHouseOffice()),
                enterpriseProxy.getPostalCode() + " " + enterpriseProxy.getTown(),
                enterpriseProxy.getCountry().toUpperCase()
        };
        for (String string : strings) {
            lineHeight = drawString(y, g2, fontHeight, xoff, lineHeight, maxWriteAreaWidth, string);
        }


    }

    private int drawString(float y, Graphics2D g2, int fontHeight, float xoff, int lineHeight, int maxWriteAreaWidth, String value) {
        List<String> wrapLines = StringUtils.wrap(value, g2.getFontMetrics(), maxWriteAreaWidth);
        for (String line : wrapLines) {
            g2.drawString(line, xoff, y + lineHeight);
            lineHeight += fontHeight;
        }
        return lineHeight;
    }



}
