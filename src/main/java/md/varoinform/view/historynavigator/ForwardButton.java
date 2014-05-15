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
 * Time: 2:53 PM
 */
public class ForwardButton extends ToolbarButton implements Observer{
    public ForwardButton(){
        super("/external-resources/icons/arrow_right2.png", "forward");
        setEnabled(false);
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                History.instance.forward();
                setEnabled(History.instance.hasForward());
            }
        });
        History.instance.addObserver(this);
    }


    @Override
    public void update(ObservableEvent event) {
        if (event.getType() == ObservableEvent.Type.HISTORY_MOVE_BACK) {
            setEnabled(History.instance.hasForward());
        }
    }
}
