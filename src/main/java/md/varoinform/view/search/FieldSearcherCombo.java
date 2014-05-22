package md.varoinform.view.search;

import md.varoinform.model.search.Searcher;

import javax.swing.*;
import java.util.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/23/14
 * Time: 2:22 PM
 */
public class FieldSearcherCombo extends JComboBox<FieldSearcherItem> {
    private List<FieldSearcherItem> fieldSearcherItems = new ArrayList<>();
    private int maxWidth = 0;

    public FieldSearcherCombo(List<Searcher> searchers) {
        for (Searcher searcher : searchers) {
            this.fieldSearcherItems.add(new FieldSearcherItem(searcher));
        }
        setMaximumRowCount(fieldSearcherItems.size());
        updateDisplay();
    }

    public void updateDisplay(){
        removeAllItems();
        Collections.sort(fieldSearcherItems);
        for (FieldSearcherItem fieldSearcherItem : fieldSearcherItems) {
            addItem(fieldSearcherItem);
            int width = fieldSearcherItem.getWidth();
            if (width > maxWidth) {
                maxWidth = width;
            }
        }

    }

    public Searcher getSearcher(){
        FieldSearcherItem item = (FieldSearcherItem) getSelectedItem();
        return item.getSearcher();
    }

    public void setSearcher(Searcher searcher){
        for (FieldSearcherItem item : fieldSearcherItems) {
            if (item.getSearcher() == searcher){
                setSelectedItem(item);
                return;
            }
        }
    }

    public int getMaxWidth() {
        return maxWidth;
    }
}

