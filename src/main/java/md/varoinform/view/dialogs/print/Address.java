package md.varoinform.view.dialogs.print;

import md.varoinform.controller.cache.Cache;
import md.varoinform.controller.cache.Field;
import md.varoinform.model.dao.EnterpriseDao;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.util.StringUtils;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: vb
 * Date: 11/23/13
 * Time: 5:25 AM
 */
public class Address extends PrintableBase {
    private final BasicStroke dashedStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{10.0f}, 0.0f);

    public Address(List<Long> enterprises, Long langID) {
        super(enterprises, langID);
        width = (int)(3.0 * inchToPTCoefficient);
        height = (int) (1.5 * inchToPTCoefficient);
    }

    @Override
    protected void drawItem(float x, float y, Graphics2D g2, Long eid) {
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


        String[] strings;
        if (langID.equals(Cache.instance.getCachedLanguage())) {
            strings = getAddressFromCache(eid);
        } else {
            Map<String, Object> map = EnterpriseDao.enterpriseAsMap(eid, langID);
            strings = getAddressFromMap(map);

        }
        for (String string : strings) {
            lineHeight = drawString(y, g2, fontHeight, xoff, lineHeight, maxWriteAreaWidth, string);
        }


    }

    private String[] getAddressFromMap(Map<String, Object> map) {
        return new String[]{
                String.valueOf(map.get(Field.title.toString())),
                StringUtils.valueOf(
                        Arrays.asList(
                                map.get(Field.street.toString()),
                                map.get(Field.house.toString()),
                                map.get(Field.office.toString()))),
                map.get(Field.postalcode.toString()) + " " + map.get(Field.town.toString()),
                getCountry(map.get(Field.country.toString()))
        };
    }

    private String getCountry(Object country) {
        if (country == null)
            country =  ResourceBundleHelper.getString(langID, "moldova", "Republica Moldova");
        return String.valueOf(country).toUpperCase();
    }

    private String[] getAddressFromCache(Long eid) {
        return new String[]{
                    String.valueOf(Cache.instance.getValue(eid, Field.title)),
                    StringUtils.valueOf(Arrays.asList(Cache.instance.getValue(eid, Field.street),
                                                      Cache.instance.getValue(eid, Field.house),
                                                      Cache.instance.getValue(eid, Field.office))),
                    Cache.instance.getValue(eid, Field.postalcode) + " " + Cache.instance.getValue(eid, Field.town),
                    getCountry(Cache.instance.getValue(eid, Field.country))
            };
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
