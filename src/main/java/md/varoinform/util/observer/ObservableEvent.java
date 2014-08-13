package md.varoinform.util.observer;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/18/13
 * Time: 4:40 PM
 */
public class ObservableEvent<T> {
    public static enum Type{
        HISTORY_MOVE_FORWARD,
        HISTORY_MOVE_BACK,
        DELETE,
        BRANCH_SELECTED,
        TAG_SELECTED,
        HISTORY_ADDED, IS_VALID, LANGUAGE_CHANGED
    }

    private Type type;
    private T value;

    public ObservableEvent(Type type, T value) {
        this.type = type;
        this.value = value;
    }

    public ObservableEvent(Type type){

        this.type = type;
    }
    public Type getType() {
        return type;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
