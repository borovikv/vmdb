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
    public static final int TAGS_CHANGED = 3;
    public static final int BRANCH_SELECTED = 4;
    public static final int BRANCH_SELECTED_BY_USER = 5;
    public static final int BACK = 6;
    public static final int FORWARD = 7;
    public static final int HOME = 8;
    public static final int TAG_SELECTED = 9;
    public static final int LANGUAGE_CHANGED = 10;
    private int type;
    private Object value;

    public ObservableEvent(int type, Object value) {
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

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
