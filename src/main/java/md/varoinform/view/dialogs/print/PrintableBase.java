package md.varoinform.view.dialogs.print;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/28/13
 * Time: 11:48 AM
 */
public abstract class PrintableBase implements Printable {
    protected final int inchToPTCoefficient = 72;
    protected int offset;
    protected List<Long> enterprises;
    protected Long langID;
    protected int width;
    protected int height;
    protected Graphics2D graphics2D;
    //protected PageFormat pageFormat;

    public PrintableBase(List<Long> enterprises, Long langID) {
        this.enterprises = enterprises;
        this.langID = langID;
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > getNumPages(pageFormat)) return NO_SUCH_PAGE;

        graphics2D = (Graphics2D) graphics;
        draw(graphics2D, pageFormat, getEnterprisesForPage(enterprises, pageIndex, pageFormat));
        return PAGE_EXISTS;
    }

    private List<Long> getEnterprisesForPage(List<Long> enterprises, int pageIndex, PageFormat pageFormat) {
        int perPage = perPage(pageFormat);
        int fromIndex = pageIndex * perPage;
        int toIndex = (pageIndex + 1) * perPage;
        toIndex = toIndex <= enterprises.size()? toIndex : enterprises.size();
        if (fromIndex > toIndex) return new ArrayList<>();
        return enterprises.subList(fromIndex, toIndex);
    }

    private void draw(Graphics2D graphics2D, PageFormat pageFormat, List<Long> enterprises){
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


    protected abstract void drawItem(float x, float y, Graphics2D g2, Long enterprise);

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

    public int getNumPages(PageFormat pageFormat){
        return (int)Math.ceil((double)enterprises.size() / perPage(pageFormat));
    }

    private int perPage(PageFormat pageFormat) {
        return countCols(pageFormat) * countRows(pageFormat);
    }
}
