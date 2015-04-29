package md.varoinform.view.dialogs.print;

import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.view.dialogs.progress.ProgressDialog;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/28/13
 * Time: 11:44 AM
 */
public class Data implements Printable {
    private final PagesActivity pagesActivity;
    private final Integer langID;


    public Data(PageFormat pageFormat, List<Integer> eid, List<String> selectedFields, Integer langID) {
        this.langID = langID;
        pagesActivity = new PagesActivity(eid, selectedFields, langID, pageFormat);
        ProgressDialog.start(pagesActivity, ResourceBundleHelper.getString("print_prepare_progress"));
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > pagesActivity.size()) return NO_SUCH_PAGE;

        Page page = pagesActivity.get(pageIndex);
        if (page == null) return NO_SUCH_PAGE;

        drawHeader((Graphics2D) graphics, pageIndex, pageFormat);
        draw((Graphics2D) graphics, pageFormat, page);

        return PAGE_EXISTS;
    }

    private void draw(Graphics2D graphics2D, PageFormat pageFormat, Page page) {
        float y = (float) pageFormat.getImageableY();
        float x = (float) pageFormat.getImageableX();
        double height = pageFormat.getImageableHeight();
        float xoff = x;
        int lineHeight = graphics2D.getFontMetrics(pagesActivity.getTitleFont()).getHeight();
        float yoff = y + lineHeight;

        for (Block block : page) {
            for (String field : block) {
                List<String> lines = block.getLines(field);
                if (lines == null) continue;
                for (String line : lines) {
                    graphics2D.setFont(block.getFont(field));
                    lineHeight = graphics2D.getFontMetrics().getHeight();
                    if (yoff > height + y){
                        xoff += pagesActivity.getBlockWidth() + pagesActivity.getHorizontalPadding();
                        lineHeight = graphics2D.getFontMetrics(pagesActivity.getTitleFont()).getHeight();
                        yoff = y + lineHeight;
                    }
                    graphics2D.drawString(line, xoff, yoff);
                    yoff += lineHeight;
                }
            }
            yoff += pagesActivity.getVerticalPadding();
        }

    }

    private void drawHeader(Graphics2D graphics2D, int pageIndex, PageFormat pageFormat) {
        float y = (float) pageFormat.getImageableY();
        float x = (float) pageFormat.getImageableX();
        String page = " " + ResourceBundleHelper.getString(langID, "page", "page: ") + (pageIndex + 1);
        FontMetrics fm = graphics2D.getFontMetrics();
        double imageableWidth = pageFormat.getImageableWidth();

        String header = getHeader(fm, imageableWidth - fm.stringWidth(page));

        int lineHeight = fm.getHeight();
        graphics2D.drawString(header, x, y - lineHeight/2);
        graphics2D.drawString(page, (float) (x + imageableWidth - fm.stringWidth(page)), y-lineHeight/2);
    }

    private String getHeader(FontMetrics fm, double width) {
        String header = ResourceBundleHelper.getString(langID, "print_header", "VMDB");

        if (fm.stringWidth(header) > width ) {
            String ellipsis = "... ";
            String builder = "";
            for (int i = 0; i < header.length(); i++) {
                builder += (header.charAt(i));
                if (fm.stringWidth(builder + ellipsis) >= width) break;
            }
            header = builder + ellipsis;
        }
        return header;
    }


    public int getNumPages() {
        return pagesActivity.size();
    }
}
