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
    List<FieldSearcherItem> fieldSearcherItems = new ArrayList<>();

    public FieldSearcherCombo(List<Searcher> searchers) {
        for (Searcher searcher : searchers) {
            this.fieldSearcherItems.add(new FieldSearcherItem(searcher));
        }
    }

    public void updateDisplay(){
        removeAllItems();
        Collections.sort(fieldSearcherItems);
        for (FieldSearcherItem fieldSearcherItem : fieldSearcherItems) {
            addItem(fieldSearcherItem);
        }

    }

    public Searcher getSearcher(){
        FieldSearcherItem item = (FieldSearcherItem) getSelectedItem();
        return item.getSearcher();
    }
}

