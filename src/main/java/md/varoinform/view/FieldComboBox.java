package md.varoinform.view;

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
public class FieldComboBox extends JComboBox<ItemSearcher> {
    List<ItemSearcher> itemSearchers = new ArrayList<>();

    public FieldComboBox(List<Searcher> searchers) {
        for (Searcher searcher : searchers) {
            this.itemSearchers.add(new ItemSearcher(searcher));
        }
    }

    public void updateDisplay(){
        removeAllItems();
        Collections.sort(itemSearchers);
        for (ItemSearcher itemSearcher : itemSearchers) {
            addItem(itemSearcher);
        }

    }

    public Searcher getSearcher(){
        ItemSearcher item = (ItemSearcher) getSelectedItem();
        return item.getSearcher();
    }
}

