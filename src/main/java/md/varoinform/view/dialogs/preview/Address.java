package md.varoinform.view.dialogs.preview;

import md.varoinform.controller.entityproxy.EnterpriseProxy;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.entities.Language;
import md.varoinform.util.StringUtils;
import md.varoinform.util.StringWrapper;

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
        width = (int)(2.5 * inchToPTCoefficient);
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

        String[] fields = {"title", "address"};
        EnterpriseProxy enterpriseProxy = new EnterpriseProxy(enterprise, language);
        for (String field : fields) {
            int maxWriteAreaWidth = (int) rectangle.getWidth() - indent * 2;
            String value = StringUtils.valueOf(enterpriseProxy.get(field));
            java.util.List<String> wrapLines = StringWrapper.wrap(value, g2.getFontMetrics(), maxWriteAreaWidth);
            for (String line : wrapLines) {
                g2.drawString(line, xoff, y + lineHeight);
                lineHeight += fontHeight;
            }
        }
    }
}
