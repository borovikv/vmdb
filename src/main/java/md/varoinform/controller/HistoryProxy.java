package md.varoinform.controller;

import md.varoinform.model.entities.Enterprise;
import md.varoinform.view.ListPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/4/13
 * Time: 12:15 PM
 */
public class HistoryProxy extends Observable implements Proxy<String> {
    private int currentIndex = -1;
    private List<Object> historyPull = new ArrayList<>();
    private ObservableEvent homeEvent;
    private ObservableEvent backDisableEvent;
    private ObservableEvent backEnableEvent;
    private ObservableEvent forwardDisableEvent;
    private ObservableEvent forwardEnableEvent;

    public HistoryProxy() {
        homeEvent = new ObservableEvent(ObservableEvent.HISTORY_MOVE, null);
        backEnableEvent = new ObservableEvent(ObservableEvent.BACK_SET_ENABLE, true);
        backDisableEvent = new ObservableEvent(ObservableEvent.BACK_SET_ENABLE, false);
        forwardEnableEvent = new ObservableEvent(ObservableEvent.FORWARD_SET_ENABLE, true);
        forwardDisableEvent = new ObservableEvent(ObservableEvent.FORWARD_SET_ENABLE, false);

    }

    @Override
    public void perform(String value) {
        switch (value){
            case "home":
                home();
                break;
            case "back":
                back();
                break;
            case "forward":
                forward();
                break;
            default:
                System.out.println(value);
                break;
        }
    }

    private void home(){
        setChanged();
        notifyObservers(homeEvent);
        append(null);

    }

    private void back(){
        if (hasBack()){
            currentIndex--;
            Object prev = historyPull.get(currentIndex);

            setChanged();
            notifyObservers(new ObservableEvent(ObservableEvent.HISTORY_MOVE, prev));
            setChanged();
            notifyObservers(forwardEnableEvent);
        }
        if (!hasBack()){
            setChanged();
            notifyObservers(backDisableEvent);
        }

    }

    private void forward(){
        if (hasForward()){
            currentIndex++;
            Object next = historyPull.get(currentIndex);

            setChanged();
            notifyObservers(new ObservableEvent(ObservableEvent.HISTORY_MOVE, next));
            setChanged();
            notifyObservers(backEnableEvent);
        }
        if (!hasForward()){
            setChanged();
            notifyObservers(forwardDisableEvent);
        }
    }

    public boolean hasBack(){
        return currentIndex >= 1;
    }

    public boolean hasForward(){
        return currentIndex < historyPull.size() - 1;
    }

    public void append(Object value){
        boolean isEndOfPull = historyPull.isEmpty() || currentIndex == historyPull.size() -1;
        if (isEndOfPull){
            historyPull.add(value);
        } else {
            historyPull = historyPull.subList(0, currentIndex+1);
            historyPull.add(value);
        }
        currentIndex++;
        if ( value instanceof String || value == null ) {
            setChanged();
            notifyObservers( new ObservableEvent( ObservableEvent.SEARCH_EVENT ) );
        }
        setChanged();
        notifyObservers(backEnableEvent);
        setChanged();
        notifyObservers(forwardDisableEvent);
    }

    public void clearHistory(){
        historyPull.clear();
        currentIndex = 0;
    }
}
