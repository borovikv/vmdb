package md.varoinform.controller;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/6/13
 * Time: 5:19 PM
 */
public class ObservableEvent {
    private int type;
    private String message;
    private Object value;

    public final static int HISTORY_MOVE = 1;
    public final static int BACK_SET_ENABLE = 2;
    public final static int FORWARD_SET_ENABLE = 3;


    public ObservableEvent(String message) {
        this.message = message;
    }

    public ObservableEvent(Object value) {
        this.value = value;
    }

    public ObservableEvent(int type) {
        this.type = type;
    }

    public ObservableEvent(String message, Object value) {
        this.message = message;
        this.value = value;
    }

    public ObservableEvent(int type, Object value) {
        this.type = type;
        this.value = value;
    }

    public ObservableEvent(int type, String message, Object value) {
        this.type = type;
        this.message = message;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ObservableEvent{" +
                "type=" + type +
                ", message='" + message + '\'' +
                ", value=" + value +
                '}';
    }
}
