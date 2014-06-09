package md.varoinform.view.historynavigator;

import md.varoinform.controller.history.History;
import md.varoinform.controller.history.HistoryEvent;
import md.varoinform.model.dao.EnterpriseDao;
import md.varoinform.util.observer.ObservableEvent;
import md.varoinform.util.observer.Observer;
import md.varoinform.view.ToolbarButton;
import md.varoinform.view.demonstrator.Demonstrator;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 5/14/14
 * Time: 2:56 PM
 */
public class HomeButton extends ToolbarButton implements Observer {
    private final Demonstrator demonstrator;
    private boolean programatically = false;

    public HomeButton(Demonstrator demonstrator) {
        super("/external-resources/icons/home.png", "home");
        this.demonstrator = demonstrator;
        History.instance.addObserver(this);
    }

    public void home(){
        demonstrator.showResults(EnterpriseDao.getEnterprises());
        if (!programatically) {
            History.instance.add(new HistoryEvent(HomeButton.this, null));
        }
    }

    @Override
    public void update(ObservableEvent event) {
        ObservableEvent.Type type = event.getType();
        Object value = event.getValue();
        if ((type == ObservableEvent.Type.HISTORY_MOVE_FORWARD
                || type == ObservableEvent.Type.HISTORY_MOVE_BACK)
                && value instanceof HistoryEvent
                && ((HistoryEvent) value).getSource() == this) {

            programatically = true;
            home();
            programatically = false;
        }
    }
}
