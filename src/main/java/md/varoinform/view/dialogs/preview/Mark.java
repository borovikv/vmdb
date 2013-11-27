package md.varoinform.view.dialogs.preview;

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
public class Mark implements Printable {

    private final int offset = 15;
    private int width = 210;
    private int height = 210;

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        Graphics2D graphics2D = (Graphics2D) graphics;

        if (pageIndex >= 1) return NO_SUCH_PAGE;
        draw(graphics2D, pageFormat, new String[][]{
                {"1I don't have time to get my head around regex",
                "I need the string\n",
                "\"Some text   with  spaces\"\n",
                "...to be converted to"},
                {"2I don't have time to get my head around regex",
                "I need the string\n",
                "\"Some text   with  spaces\"\n",
                "...to be converted to"},
                {"3I don't have time to get my head around regex",
                "I need the string\n",
                "\"Some text   with  spaces\"\n",
                "...to be converted to"},
                {"4I don't have time to get my head around regex",
                "I need the string\n",
                "\"Some text   with  spaces\"\n",
                "...to be converted to"},
                {"5 I don't have time to get my head around regex",
                "I need the string\n",
                "\"Some text   with  spaces\"\n",
                "...to be converted to"},
                {"6 I don't have time to get my head around regex",
                "I need the string\n",
                "\"Some text   with  spaces\"\n",
                "...to be converted to"},
                {"7 I don't have time to get my head around regex",
                "I need the string\n",
                "\"Some text   with  spaces\"\n",
                "...to be converted to"},
                {"8 I don't have time to get my head around regex",
                "I need the string\n",
                "\"Some text   with  spaces\"\n",
                "...to be converted to"}, });
        return PAGE_EXISTS;
    }

    private void draw(Graphics2D graphics2D, PageFormat pageFormat, String[][] infoLines){
        float y = (float) pageFormat.getImageableY();
        float x = (float) pageFormat.getImageableX();
        int hc = countHeight(pageFormat);
        int wc = countWidth(pageFormat);
        float yoff = y;
        float xoff = x;
        for (int i1 = 0, infoLinesLength = infoLines.length; i1 < infoLinesLength; i1++) {

            String[] infoLine = infoLines[i1];

            int i = i1 % wc;
            if (i1!= 0 && i == 0){
                yoff += height + offset;
                xoff = x;
            }
            xoff += width * i  + offset * i;

            drawItem(xoff, yoff, graphics2D, infoLine);
        }


    }
    private void drawItem(float x, float y, Graphics2D g2, String[] lines) {
        Rectangle2D rectangle = new Rectangle2D.Double(x, y, width, height);
        Font f = new Font("Serif", Font.PLAIN, 14);
        g2.setFont(f);
        g2.draw(rectangle);
        int indent = 15;
        int fontHeight = g2.getFontMetrics().getHeight();
        float xoff = x + indent;
        float yoff = y;
        int lineHeight = fontHeight + indent;
        for (String line : lines) {
            int maxWriteAreaWidth = (int) rectangle.getWidth() - indent * 2;
            List<String> wrapLines = StringUtils.wrap(line, g2.getFontMetrics(), maxWriteAreaWidth);
            for (String wrapLine : wrapLines) {
                g2.drawString(wrapLine, xoff, yoff + lineHeight);
                lineHeight += indent * 2;
            }
        }

    }

    private int countWidth(PageFormat pageFormat){
         return (int) (
                 ( pageFormat.getImageableWidth() + offset )
                         /
                         ( width + offset )
         );
    }

    private int countHeight(PageFormat pageFormat) {
        return (int) ((pageFormat.getImageableHeight() + offset)/ (height + offset));
    }

    public int getPageCount(PageFormat pageFormat){
        return countHeight(pageFormat) * countWidth(pageFormat);
    }
}
