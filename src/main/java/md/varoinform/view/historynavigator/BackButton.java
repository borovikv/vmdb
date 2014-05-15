package md.varoinform.view.historynavigator;

import md.varoinform.controller.history.History;
import md.varoinform.util.ObservableEvent;
import md.varoinform.util.Observer;
import md.varoinform.view.ToolbarButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 5/14/14
 * Time: 2:52 PM
 */
public class BackButton extends ToolbarButton implements Observer {
    public BackButton() {
        super("/external-resources/icons/arrow_left2.png", "back");
        setEnabled(false);
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                History.instance.back();
                setEnabled(History.instance.hasBack());
            }
        });
        History.instance.addObserver(this);
    }


    @Override
    public void update(ObservableEvent event) {
        ObservableEvent.Type type = event.getType();
        if (type == ObservableEvent.Type.HISTORY_MOVE_FORWARD || type == ObservableEvent.Type.HISTORY_ADDED){
            setEnabled(History.instance.hasBack());
        }
    }
}
