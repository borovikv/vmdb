package md.varoinform.view.navigation.search;

import md.varoinform.controller.history.History;
import md.varoinform.controller.history.HistoryEvent;
import md.varoinform.model.search.Searcher;
import md.varoinform.model.search.Searchers;
import md.varoinform.util.observer.ObservableEvent;
import md.varoinform.util.observer.Observer;
import md.varoinform.view.ToolbarButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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

    public SearchPanel() {
        ActionListener searchAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                search(searcherCombo.getSearcher(), searchField.getText());
            }
        };

        searchButton = new ToolbarButton("/external-resources/icons/search.png", "search", "search");
        searchButton.addActionListener(searchAction);

        searchField = new SearchField();
        searchField.addActionListener(searchAction);

        searcherCombo = new FieldSearcherCombo(Searchers.getSearchers());

        History.instance.addObserver(this);
    }

    public void search(Searcher searcher, String text) {
        Component rootPane = SwingUtilities.getRoot(searchField);
        rootPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        List<Long> enterprises = searchText(searcher, text);
        rootPane.setCursor(Cursor.getDefaultCursor());
        fireSearchEnded(enterprises);
    }

    public void fireSearchEnded(List<Long> enterprises) {
        for (SearchListener listener : listeners) {
            listener.perform(enterprises);
        }
    }

    private List<Long> searchText(Searcher searcher, String text) {
        if (text == null) return null;
        HistoryEvent event = new HistoryEvent(searcher, text);
        java.util.List<Long> enterprises;
        enterprises = searcher.search(text);
        History.instance.add(event);
        return enterprises;
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
                search((Searcher) source, text);
                searchField.setText(text);
            } else {
                searchField.setText("");

            }
        }

    }

}
