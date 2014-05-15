package md.varoinform.controller.history;

import md.varoinform.util.Observable;
import md.varoinform.util.ObservableEvent;
import md.varoinform.util.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 5/14/14
 * Time: 1:03 PM
 */
public enum History implements Observable{
    instance;

    private List<HistoryEvent> history = new ArrayList<>();
    private int currentIndex = -1;
    private List<Observer> observers = new ArrayList<>();

    public void add(HistoryEvent event){
        int size = history.size();
        int lastIndex = size - 1;

        if (currentIndex >=0 && size >= currentIndex && !history.isEmpty()
                && history.get(currentIndex).equals(event)) return;

        if (history.isEmpty() || currentIndex == lastIndex){
            history.add(event);

        } else {
            history = history.subList(0, currentIndex + 1);
            history.add(event);
        }

        currentIndex++;
        notifyObservers(new ObservableEvent(ObservableEvent.Type.HISTORY_ADDED));
    }

    public void back(){
        if (hasBack()){
            currentIndex--;
            HistoryEvent historyEvent = history.get(currentIndex);
            notifyObservers(new ObservableEvent(ObservableEvent.Type.HISTORY_MOVE_BACK, historyEvent));
        }
    }

    public boolean hasBack(){
        return currentIndex >= 1;
    }

    public void forward(){
        if (hasForward()){
            currentIndex++;
            HistoryEvent historyEvent = history.get(currentIndex);
            notifyObservers(new ObservableEvent(ObservableEvent.Type.HISTORY_MOVE_FORWARD, historyEvent));
        }
    }

    public boolean hasForward(){
        return currentIndex < history.size() - 1;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers(ObservableEvent event) {
        for (Observer observer : observers) {
            observer.update(event);
        }
    }
}
