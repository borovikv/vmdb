package md.varoinform.view.navigation.search;

import md.varoinform.model.search.Searcher;
import md.varoinform.util.ResourceBundleHelper;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/23/14
 * Time: 3:09 PM
 */
class FieldSearcherItem extends DefaultListCellRenderer implements Comparable<FieldSearcherItem>  {
    private Searcher searcher;

    public FieldSearcherItem(Searcher searcher) {
        if (searcher == null) throw new NullPointerException();
        this.searcher = searcher;

    }

    public String getNameDisplay() {
        String name = searcher.getName();
        return ResourceBundleHelper.getString(name, name);
    }

    @Override
    public String toString() {
        return getNameDisplay();
    }

    @Override
    public int compareTo(FieldSearcherItem o) {
        if (o == null) throw new NullPointerException();
        if (searcher.getName().equalsIgnoreCase("default")) return -1;
        if (o.getSearcher().getName().equalsIgnoreCase("default")) return 1;

        return getNameDisplay().compareTo(o.getNameDisplay());
    }

    public int getWidth(){
        FontMetrics metrics = getFontMetrics(getFont());
        return metrics.stringWidth(toString());
    }
    public Searcher getSearcher() {
        return searcher;
    }
}
