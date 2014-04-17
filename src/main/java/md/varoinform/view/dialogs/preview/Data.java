package md.varoinform.view.dialogs.preview;

import md.varoinform.controller.comparators.ColumnPriorityComparator;
import md.varoinform.controller.entityproxy.EnterpriseProxy;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.entities.Language;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.util.StringUtils;
import md.varoinform.util.StringWrapper;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/28/13
 * Time: 11:44 AM
 */
public class Data extends PrintableBase {
    private final List<String> selectedFields;
    private static final int indent = 15;
    private static final Font PLAIN = new Font("SanSerif", Font.PLAIN, 10);
    //private int counter = 0;

    public Data(List<Enterprise> enterprises, List<String> selectedFields, Language language) {
        super(enterprises, language);
        this.selectedFields = new ArrayList<>(selectedFields);
        Collections.sort(this.selectedFields, new ColumnPriorityComparator());
        height = inchToPTCoefficient * 2;
        offset = 15;
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        width = getImageableWidth(pageFormat);
        //counter = 0;
        return super.print(graphics, pageFormat, pageIndex);
    }

    @Override
    protected void drawItem(float x, float y, Graphics2D g2, Enterprise enterprise) {
        g2.setFont(PLAIN);
        //counter++;
        int fontHeight = g2.getFontMetrics().getHeight();
        float xoff = x + indent;
        int lineHeight = fontHeight;
        List<String> lines = getLines(width, g2.getFontMetrics(), enterprise);

        //height = lineHeight*lines.size() + indent;
        for (String line : lines) {
            g2.drawString(line, xoff, y + lineHeight);
            lineHeight += fontHeight;
        }

        Rectangle2D rectangle = new Rectangle2D.Double(x, y, width, height);
        g2.draw(rectangle);
    }

    private List<String> getLines(int width, FontMetrics fm, Enterprise enterprise) {
        List<String> lines = new ArrayList<>();
        EnterpriseProxy enterpriseProxy = new EnterpriseProxy(enterprise, language);
        for (String selectedField : selectedFields) {
            String str = "";
            int maxWriteAreaWidth = width - indent * 2;
            String value = StringUtils.valueOf(enterpriseProxy.get(selectedField));
            str += ResourceBundleHelper.getString(language, selectedField, selectedField) + ": "
                    + StringUtils.getStringOrNA(value, language);
            List<String> wrapLines = StringWrapper.wrap(str, fm, maxWriteAreaWidth);
            lines.addAll(wrapLines);
        }
        return lines;
    }

    public int getPageCount(Graphics2D g2, PageFormat pageFormat) {
        int linesCounter = 0;
        for (Enterprise enterprise : enterprises) {
            linesCounter += getLines(getImageableWidth(pageFormat), g2.getFontMetrics(), enterprise).size();
        }

        double imageableHeight = pageFormat.getImageableHeight();

        return (int) Math.ceil(linesCounter * g2.getFontMetrics().getHeight() / imageableHeight);
    }

    private int getImageableWidth(PageFormat pageFormat) {
        int imageableWidth = (int) pageFormat.getImageableWidth();
        int maxWidth = 7 * inchToPTCoefficient;
        return imageableWidth < maxWidth ? imageableWidth : maxWidth;
    }

}
