package md.varoinform.view.dialogs.preview;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

/**
 * Created with IntelliJ IDEA.
 * User: vb
 * Date: 11/21/13
 * Time: 3:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class CoverPage implements Printable {
    private String title;

    public CoverPage(String title) {
        this.title = title;
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex >= 1) return Printable.NO_SUCH_PAGE;
        Graphics2D g2 = (Graphics2D) graphics;
        g2.setPaint(Color.black);
        g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        FontRenderContext context = g2.getFontRenderContext();
        Font f = g2.getFont();

        TextLayout layout = new TextLayout(title, f, context);
        // y coordinate
        float ascent = layout.getAscent();
        g2.drawString(title, 0, ascent);
        return Printable.PAGE_EXISTS;
    }
}
