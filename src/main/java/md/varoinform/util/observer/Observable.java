package md.varoinform.util.observer;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/18/13
 * Time: 4:34 PM
 */
public interface Observable {
    public void addObserver(Observer observer);
    public void notifyObservers(ObservableEvent event);
}
