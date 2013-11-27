package md.varoinform.view.dialogs.preview;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

/**
 * Created with IntelliJ IDEA.
 * User: vb
 * Date: 11/21/13
 * Time: 3:49 PM
 */
public class Banner implements Printable {
    private String message;
    private double scale;

    public Banner(String message) {

        this.message = message;
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        Graphics2D g2 = (Graphics2D) graphics;
        if (pageIndex > getPageCount(g2, pageFormat)) {
            return Printable.NO_SUCH_PAGE;
        }
        g2.translate(pageFormat.getImageableX(),pageFormat.getImageableY());
        drawPage(g2, pageFormat, pageIndex);
        return Printable.PAGE_EXISTS;
    }

    private void drawPage(Graphics2D g2, PageFormat pageFormat, int page) {
        if (message.equals("")) return;

        page--;

        drawCropMarks(g2, pageFormat);
        g2.clip(new Rectangle2D.Double(0, 0, pageFormat.getImageableWidth(), pageFormat.getImageableHeight()));
        g2.translate(-page*pageFormat.getImageableWidth(), 0);
        g2.scale(scale, scale);
        FontRenderContext context = g2.getFontRenderContext();
        Font f = new Font("Serif", Font.PLAIN, 72);
        TextLayout layout = new TextLayout(message, f, context);
        AffineTransform transform = AffineTransform.getTranslateInstance(0, layout.getAscent());
        Shape outline = layout.getOutline(transform);
        g2.draw(outline);
    }

    private void drawCropMarks(Graphics2D g2, PageFormat pageFormat) {
        final double C = 36;
        double w = pageFormat.getImageableWidth();
        double h = pageFormat.getImageableHeight();

        g2.draw(new Line2D.Double(0, 0, 0, C));
        g2.draw(new Line2D.Double(0, 0, C, 0));
        g2.draw(new Line2D.Double(w, 0, w, C));
        g2.draw(new Line2D.Double(w, 0, w - C, 0));
        g2.draw(new Line2D.Double(0, h, 0, h - C));
        g2.draw(new Line2D.Double(0, h, C, h));
        g2.draw(new Line2D.Double(w, h, w, h - C));
        g2.draw(new Line2D.Double(w, h, w - C, h));

    }

    public int getPageCount(Graphics2D graphics, PageFormat pageFormat) {
        if (message.equals("")) {
            return 0;
        }

        FontRenderContext context = graphics.getFontRenderContext();
        Font f = new Font("Serif", Font.PLAIN, 72);
        Rectangle2D bounds = f.getStringBounds(message,context);
        scale = pageFormat.getImageableHeight() / bounds.getHeight();
        double width = scale * bounds.getWidth();
        int pages = (int)Math.ceil(width / pageFormat.getImageableWidth());
        return pages;
    }


}
