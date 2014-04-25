package md.varoinform.view;

import md.varoinform.model.search.Searcher;
import md.varoinform.util.ResourceBundleHelper;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/23/14
 * Time: 3:09 PM
 */
class ItemSearcher extends DefaultListCellRenderer implements Comparable<ItemSearcher>  {
    private Searcher searcher;

    public ItemSearcher(Searcher searcher) {
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
    public int compareTo(ItemSearcher o) {
        if (o == null) throw new NullPointerException();
        if (searcher.getName().equalsIgnoreCase("default")) return -1;
        if (o.getSearcher().getName().equalsIgnoreCase("default")) return 1;

        return getNameDisplay().compareTo(o.getNameDisplay());
    }

    public Searcher getSearcher() {
        return searcher;
    }
}
