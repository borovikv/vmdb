package md.varoinform.controller;

import md.varoinform.model.entities.Enterprise;
import md.varoinform.view.ListPanel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/4/13
 * Time: 12:15 PM
 */
public class HistoryProxy implements Proxy<String> {
    private Demonstrator demonstrator;
    private int currentIndex = -1;
    private List<List<Enterprise>> historyPull = new ArrayList<>();

    public HistoryProxy(Demonstrator demonstrator) {
        this.demonstrator = demonstrator;
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
        demonstrator.clear();
        append(new ArrayList<Enterprise>());

    }

    private void back(){
        System.out.println(hasBack());
        if (hasBack()){
            currentIndex--;
            List<Enterprise> enterprises = historyPull.get(currentIndex);
            demonstrator.showResults(enterprises);
        }
    }

    private void forward(){
        if (hasForward()){
            currentIndex++;
            List<Enterprise> enterprises = historyPull.get(currentIndex);
            demonstrator.showResults(enterprises);
        }
    }

    public boolean hasBack(){
        return currentIndex >= 1;
    }

    public boolean hasForward(){
        return currentIndex < historyPull.size() - 1;
    }

    public void append(List<Enterprise> enterprises){
        boolean isEndOfPull = historyPull.isEmpty() || currentIndex == historyPull.size() -1;
        if (isEndOfPull){
            historyPull.add(enterprises);
        } else {
            historyPull = historyPull.subList(0, currentIndex+1);
            historyPull.add(enterprises);
        }
        currentIndex++;
    }

    public void clear(){
        historyPull.clear();
        currentIndex = 0;
    }
}
