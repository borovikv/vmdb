package md.varoinform.view.navigation.search;

import md.varoinform.controller.history.History;
import md.varoinform.controller.history.HistoryEvent;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.search.Searcher;
import md.varoinform.model.search.Searchers;
import md.varoinform.util.Profiler;
import md.varoinform.util.observer.ObservableEvent;
import md.varoinform.util.observer.Observer;
import md.varoinform.view.ToolbarButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 5/15/14
 * Time: 11:40 AM
 */
public class SearchPanel implements Observer {
    public final SearchField searchField;
    public final FieldSearcherCombo searcherCombo;
    public final ToolbarButton searchButton;
    private final List<SearchListener> listeners = new ArrayList<>();
    private final Map<HistoryEvent, List<Enterprise>> cache = new HashMap<>();

    public SearchPanel() {
        ActionListener searchAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Profiler p = new Profiler();
                Searcher searcher = searcherCombo.getSearcher();
                searchText(searcher, searchField.getText());
                p.end();
            }
        };

        searchButton = new ToolbarButton("/external-resources/icons/search.png", "search", "search");
        searchButton.addActionListener(searchAction);

        searchField = new SearchField();
        searchField.addActionListener(searchAction);

        searcherCombo = new FieldSearcherCombo(Searchers.getSearchers());

        History.instance.addObserver(this);
    }

    private void searchText(Searcher searcher, String text) {
        if (text == null) return;
        HistoryEvent event = new HistoryEvent(searcher, text);
        java.util.List<Enterprise> enterprises;
        if (cache.containsKey(event)) {
            enterprises = cache.get(event);
        } else {
            enterprises = searcher.search(text);
        }
        History.instance.add(event);
        cache(event, enterprises);
        for (SearchListener listener : listeners) {
            listener.perform(enterprises);
        }
    }

    private void cache(HistoryEvent value, List<Enterprise> enterprises) {
        if (cache.containsKey(value)) return;
        cache.put(value, enterprises);
    }

    public void addSearchAction(SearchListener listener){
        listeners.add(listener);
    }

    public void updateDisplay(){
        searchButton.updateDisplay();
        searcherCombo.updateDisplay();
    }

    @Override
    public void update(ObservableEvent event) {
        ObservableEvent.Type type = event.getType();
        Object value = event.getValue();

        if ((type == ObservableEvent.Type.HISTORY_MOVE_FORWARD || type == ObservableEvent.Type.HISTORY_MOVE_BACK)
                && value instanceof HistoryEvent){

            Object source = ((HistoryEvent) value).getSource();
            if (source instanceof Searcher){
                searcherCombo.setSearcher((Searcher) source);
                String text = (String) ((HistoryEvent) value).getState();
                searchText((Searcher) source, text);
                searchField.setText(text);
            } else {
                searchField.setText("");

            }
        }

    }
}
