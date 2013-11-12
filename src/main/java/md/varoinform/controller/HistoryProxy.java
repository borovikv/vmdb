package md.varoinform.controller;

import java.util.ArrayList;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/4/13
 * Time: 12:15 PM
 */
public class HistoryProxy {
    private int currentIndex = -1;
    private List<Object> historyPull = new ArrayList<>();

    public void home(){
        append(null);

    }

    public Object back(){
        if (hasBack()){
            currentIndex--;
            return historyPull.get(currentIndex);
        }
        return null;
    }

    public Object forward(){
        if (hasForward()){
            currentIndex++;
            return historyPull.get(currentIndex);
        }
        return null;
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

    }

    @SuppressWarnings("UnusedDeclaration")
    public void clearHistory(){
        historyPull.clear();
        currentIndex = 0;
    }
}
