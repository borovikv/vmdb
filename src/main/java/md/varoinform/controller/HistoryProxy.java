package md.varoinform.controller;

import md.varoinform.model.entities.Enterprise;
import md.varoinform.view.ListPanel;

import javax.swing.*;
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
    private JButton home;
    private final JButton back;
    private final JButton forward;
    private int currentIndex = -1;
    private List<List<Enterprise>> historyPull = new ArrayList<>();

    public HistoryProxy(Demonstrator demonstrator, JButton home, JButton back, JButton forward) {
        this.demonstrator = demonstrator;
        this.home = home;
        this.back = back;
        this.forward = forward;
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
        if (hasBack()){
            currentIndex--;
            List<Enterprise> enterprises = historyPull.get(currentIndex);
            demonstrator.showResults(enterprises);
            forward.setEnabled(true);
        }
        if (!hasBack()){
            back.setEnabled(false);
        }
    }

    private void forward(){
        if (hasForward()){
            currentIndex++;
            List<Enterprise> enterprises = historyPull.get(currentIndex);
            demonstrator.showResults(enterprises);
            back.setEnabled(true);
        }
        if (!hasForward()){
            forward.setEnabled(false);
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
        back.setEnabled(true);
        forward.setEnabled(false);
    }

    public void clear(){
        historyPull.clear();
        currentIndex = 0;
    }
}
