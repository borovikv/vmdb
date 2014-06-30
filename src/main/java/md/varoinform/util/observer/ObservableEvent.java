package md.varoinform.util.observer;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/18/13
 * Time: 4:40 PM
 */
public class ObservableEvent {
    public static enum Type{
        HISTORY_MOVE_FORWARD,
        HISTORY_MOVE_BACK,
        DELETE,
        TAGS_CHANGED,
        BRANCH_SELECTED,
        TAG_SELECTED,
        HISTORY_ADDED, CLEAR_DEMONSTRATOR, IS_VALID, LANGUAGE_CHANGED
    }

    private Type type;
    private Object value;

    public ObservableEvent(Type type, Object value) {
        this.type = type;
        this.value = value;
    }

    public ObservableEvent(Type type){

        this.type = type;
    }
    public Type getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
