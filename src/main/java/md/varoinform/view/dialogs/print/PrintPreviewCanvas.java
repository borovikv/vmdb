package md.varoinform.view.dialogs.print;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

/**
 * Created with IntelliJ IDEA.
 * User: vb
 * Date: 11/22/13
 * Time: 8:13 AM
 */
public class PrintPreviewCanvas extends JPanel{
    private int currentPage;
    private Book book;

    public PrintPreviewCanvas(Book book) {
        this.book = book;
        this.currentPage = 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(book.getNumberOfPages() == 0) return;
        Graphics2D g2 = (Graphics2D) g;
        PageFormat pageFormat = book.getPageFormat(currentPage);

        double xoff;
        double yoff;
        double scale;

        double px = pageFormat.getWidth();
        double py = pageFormat.getHeight();
        double sx = getWidth() - 1;
        double sy = getHeight() - 1;

        if (px / py < sx / sy) {   //центрирование по горизонтали
            scale = sy / py;
            xoff = 0.5 * (sx - scale * px);
            yoff = 0;
        } else {
            scale = sx / px;
            xoff = 0;
            yoff = 0.5 * (sy - scale * py);
        }

        g2.translate((float)xoff, (float)yoff);
        g2.scale(scale, scale);

        Rectangle2D page = new Rectangle2D.Double(0, 0, px, py);
        g2.setPaint(Color.white);
        g2.fill(page);
        g2.setPaint(Color.BLACK);
        g2.draw(page);

        Printable printable = book.getPrintable(currentPage);
        try {
            printable.print(g2, pageFormat, currentPage);
        } catch (PrinterException e) {
            g2.draw(new Line2D.Double(0, 0, px, py));
            g2.draw(new Line2D.Double(px, 0, 0, py));
        }
    }

    public void flipPage(int i) {
        int newPage = currentPage + i;
        if (0<= newPage && newPage < book.getNumberOfPages()){
            currentPage = newPage;
            repaint();
        }
    }

    public int getPage() {
        return currentPage;
    }
}
