package md.varoinform.view.historynavigator;

import md.varoinform.controller.history.EventSource;
import md.varoinform.controller.history.History;
import md.varoinform.controller.history.HistoryEvent;
import md.varoinform.model.dao.EnterpriseDao;
import md.varoinform.view.ToolbarButton;
import md.varoinform.view.demonstrator.Demonstrator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 5/14/14
 * Time: 2:56 PM
 */
public class HomeButton extends ToolbarButton implements EventSource{
    private final Demonstrator demonstrator;
    private boolean programatically = false;

    public HomeButton(Demonstrator demonstrator) {
        super("/external-resources/icons/home.png", "home");
        this.demonstrator = demonstrator;
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                home();
            }
        });
    }

    public void home(){
        demonstrator.showResults(EnterpriseDao.getEnterprises());
        if (!programatically) {
            History.instance.add(new HistoryEvent(HomeButton.this, null));
        }
    }

    @Override
    public void checkout(Object state) {
        programatically = true;
        home();
        programatically = false;
    }
}
