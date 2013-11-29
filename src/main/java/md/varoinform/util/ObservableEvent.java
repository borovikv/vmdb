package md.varoinform.util;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/18/13
 * Time: 4:40 PM
 */
public class ObservableEvent {
    public static final int STRUCTURE_CHANGED = 1;
    public static final int DELETE = 2;
    private int type;
    private String value;

    @SuppressWarnings("UnusedDeclaration")
    public ObservableEvent(int type, String value) {
        this.type = type;
        this.value = value;
    }

    public ObservableEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
