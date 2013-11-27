package md.varoinform.view.dialogs.preview;

import md.varoinform.controller.entityproxy.EnterpriseProxy;
import md.varoinform.model.entities.Enterprise;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vb
 * Date: 11/23/13
 * Time: 5:25 AM
 */
public class Address implements Printable {
    private final int inchToPTCoefficient = 72;
    private final int offset = 0;
    private final BasicStroke dashedStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{10.0f}, 0.0f);
    private int width = (int)(2.5 * inchToPTCoefficient);
    private int height = (int) (1.5 * inchToPTCoefficient);
    private List<Enterprise> enterprises;

    public Address(List<Enterprise> enterprises) {
        this.enterprises = enterprises;
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > getPageCount(pageFormat)) return NO_SUCH_PAGE;

        Graphics2D graphics2D = (Graphics2D) graphics;
        draw(graphics2D, pageFormat, getEnterprisesForPage(enterprises, pageIndex, pageFormat));
        return PAGE_EXISTS;
    }

    private List<Enterprise> getEnterprisesForPage(List<Enterprise> enterprises, int pageIndex, PageFormat pageFormat) {
        int perPage = perPage(pageFormat);
        int fromIndex = pageIndex * perPage;
        int toIndex = (pageIndex + 1) * perPage;
        toIndex = toIndex <= enterprises.size()? toIndex : enterprises.size();
        return enterprises.subList(fromIndex, toIndex);
    }

    private void draw(Graphics2D graphics2D, PageFormat pageFormat, List<Enterprise> enterprises){
        float y = (float) pageFormat.getImageableY();
        float x = (float) pageFormat.getImageableX();
        int columnsNumber = countCols(pageFormat);
        float yoff = y;
        float xoff;
        for (int i = 0, enterprisesSize = enterprises.size(); i < enterprisesSize; i++) {
            int column = i % columnsNumber;
            xoff = x + width * column  + offset * column;
            if (i!= 0 && column == 0){
                yoff += height + offset;
            }
            drawItem(xoff, yoff, graphics2D, enterprises.get(i));
        }


    }
    private void drawItem(float x, float y, Graphics2D g2, Enterprise enterprise) {
        Rectangle2D rectangle = new Rectangle2D.Double(x, y, width, height);
        Font f = new Font("SanSerif", Font.PLAIN, 10);
        g2.setFont(f);
        g2.setStroke(dashedStroke);
        g2.draw(rectangle);

        int indent = 15;
        int fontHeight = g2.getFontMetrics().getHeight();
        float xoff = x + indent;
        int lineHeight = fontHeight;

        String[] fields = {"title", "address"};
        EnterpriseProxy enterpriseProxy = new EnterpriseProxy(enterprise);
        for (String field : fields) {
            int maxWriteAreaWidth = (int) rectangle.getWidth() - indent * 2;
            List<String> wrapLines = StringUtils.wrap(enterpriseProxy.get(field), g2.getFontMetrics(), maxWriteAreaWidth);
            for (String line : wrapLines) {
                g2.drawString(line, xoff, y + lineHeight);
                lineHeight += fontHeight;
            }
        }
    }

    private int countCols(PageFormat pageFormat){
         return (int) (
                 ( pageFormat.getImageableWidth() + offset )
                         /
                         ( width + offset )
         );
    }

    private int countRows(PageFormat pageFormat) {
        return (int) ((pageFormat.getImageableHeight() + offset)/ (height + offset));
    }

    public int getPageCount(PageFormat pageFormat){
        return (int)Math.ceil((double)enterprises.size() / perPage(pageFormat));
    }

    private int perPage(PageFormat pageFormat) {
        return countCols(pageFormat) * countRows(pageFormat);
    }
}
