package md.varoinform.controller.history;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 5/14/14
 * Time: 1:04 PM
 */
public class HistoryEvent {
    private final Object source;
    private final Object state;

    public HistoryEvent(Object source, Object state) {
        this.source = source;
        this.state = state;
    }

    public Object getSource() {
        return source;
    }

    public Object getState() {
        return state;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof HistoryEvent){
            HistoryEvent event = (HistoryEvent) obj;
            return (source == event.source || source != null && source.equals(event.source))
                    && (state == event.state || state != null && state.equals(event.state));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31*result + source.hashCode();
        result = 31*result + state.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "HistoryEvent{" +
                "source=" + source +
                ", state=" + state +
                '}';
    }
}
