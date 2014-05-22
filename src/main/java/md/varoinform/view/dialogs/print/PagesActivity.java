package md.varoinform.view.dialogs.print;

import md.varoinform.controller.entityproxy.EnterpriseProxy;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.entities.Language;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.util.StringUtils;
import md.varoinform.util.StringWrapper;
import md.varoinform.view.dialogs.progress.Activity;

import java.awt.*;
import java.awt.print.PageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: Vladimir Borovic
 * Date: 5/20/14
 * Time: 9:40 AM
 */
public class PagesActivity extends Activity {
    private static final Font TITLE_FONT = new Font("SanSerif", Font.BOLD, 10);
    private final double canvasWidth;
    private final double canvasHeight;
    private List<Page> pages = new ArrayList<>();
    private final List<Enterprise> enterprises;
    private final Language language;
    private static final Font DEFAULT_FONT = new Font("SanSerif", Font.PLAIN, 10);
    private List<String> fields;
    private static final int horizontalPadding = 15;
    private static final int verticalPadding = 15;
    private final FontMetrics defaultFM;
    private final FontMetrics titleFM;
    private double blockWidth;
    private int counter = 1;
    private String format;


    public PagesActivity(List<Enterprise> enterprises, List<String> fields, Language language, PageFormat pageFormat) {
        this.enterprises = enterprises;
        this.language = language;
        this.fields = fields;
        Canvas canvas = new Canvas();
        defaultFM = canvas.getFontMetrics(DEFAULT_FONT);
        titleFM = canvas.getFontMetrics(TITLE_FONT);
        canvasWidth = pageFormat.getImageableWidth();
        canvasHeight = pageFormat.getImageableHeight();
        format = ResourceBundleHelper.getString("prepared_i_from_size", super.getFormat());
    }

    public int getHorizontalPadding() {
        return horizontalPadding;
    }


    @Override
    protected Void doInBackground() throws Exception {
        Page page = new Page();
        pages.add(page);
        blockWidth = calculateBlockWidth(canvasWidth);
        double canvasArea = calculateEffectiveArea(blockWidth, canvasWidth, canvasHeight);

        int usedArea = 0;
        int size = enterprises.size();
        for (int i = 0; i < size; i++) {

            Block block = getBlock(enterprises.get(i), language);
            double blockArea = (block.height() + verticalPadding) * (blockWidth + horizontalPadding);
            usedArea += blockArea;

            if (usedArea > canvasArea) {
                page = new Page();
                pages.add(page);
                usedArea = (int) blockArea;
            }
            page.addBlock(block);


            int progress = i * 100 / size;
            setProgress(progress);
            setNote(i, size);
        }
        return null;
    }

    @Override
    public String getFormat() {
        return format;
    }

    private double calculateEffectiveArea(double innerBlockWidth, double canvasWidth, double canvasHeight) {
        if (canvasWidth == innerBlockWidth) return canvasHeight * canvasWidth;
        double outerBlockWidth = innerBlockWidth + horizontalPadding;
        int blocksByHorizontal = (int) Math.floor(canvasWidth / outerBlockWidth);
        return canvasHeight * blocksByHorizontal * outerBlockWidth;
    }

    private double calculateBlockWidth(double canvasWidth) {
        int inchToPT = 72;
        double width = 3.1 * inchToPT;
        return (width + horizontalPadding) * 2 > canvasWidth ? canvasWidth : width;
    }

    private Block getBlock(Enterprise enterprise, Language language) {
        Block block = new Block();

        EnterpriseProxy enterpriseProxy = new EnterpriseProxy(enterprise, language);
        for (String field : fields) {
            if (EnterpriseProxy.isAddress(field)) continue;

            String str = getLine(language, enterpriseProxy.get(field), field);
            if (str.isEmpty()) continue;

            FontMetrics fm;
            if (EnterpriseProxy.isTitle(field)) {
                str = counter + ". " + str;
                counter++;
                fm = titleFM;
            } else {
                fm = defaultFM;
            }

            List<String> wrapLines = StringWrapper.wrap(str, fm, (int) blockWidth);

            if (EnterpriseProxy.isTitle(field)) {
                block.addLines(field, wrapLines, TITLE_FONT);
            } else {
                block.addLines(field, wrapLines, DEFAULT_FONT);
            }
        }

        String address = getAddress(fields, enterpriseProxy, language);
        if (address.isEmpty()) return block;
        List<String> addressLines = StringWrapper.wrap(address, defaultFM, (int) blockWidth);
        block.addLines("address", addressLines, DEFAULT_FONT);

        return block;
    }

    private String getAddress(List<String> fields, EnterpriseProxy enterpriseProxy, Language language) {
        List<String> addressFields = EnterpriseProxy.getAddressFields();
        List<Object> address = new ArrayList<>();
        for (String field : addressFields) {
            if (fields.contains(field)){
                Object o = enterpriseProxy.get(field);
                if (o instanceof Collection){
                    address.addAll((Collection<?>) o);
                } else {
                    address.add(o);
                }
            }
        }

        return getLine(language, address, "address");
    }

    private String getLine(Language language, Object o, String field) {
        String value = StringUtils.valueOf(o);
        if (value.isEmpty()) return "";

        if (EnterpriseProxy.isTitle(field)) return value;

        String label = ResourceBundleHelper.getString(language, field, field);
        return label + ": " + value;
    }

    public int size() {
         return pages.size();
    }

    public Page get(int pageIndex) {
        if (pageIndex >= 0 && pages.size() > pageIndex) {
            return pages.get(pageIndex);
        }
        return null;
    }

    public double getBlockWidth() {
        return blockWidth;
    }

    public float getVerticalPadding() {
        return verticalPadding;
    }

    public Font getTitleFont() {
        return TITLE_FONT;
    }

}
