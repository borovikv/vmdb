package md.varoinform.view.dialogs;

import md.varoinform.model.dao.DAOTag;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.util.Observable;
import md.varoinform.util.ObservableEvent;
import md.varoinform.util.Observer;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.view.tags.TagPanel;
import md.varoinform.view.demonstrator.Demonstrator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/29/13
 * Time: 9:56 AM
 */
public class TagDialog extends JDialog implements Observable, Observer {
    private final TagPanel tagPanel = new TagPanel();
    private final Demonstrator demonstrator;
    private List<Observer> observers = new ArrayList<>();


    public TagDialog(Component parent, Demonstrator demonstrator) {
        this.demonstrator = demonstrator;
        setLocationRelativeTo(parent);
        setModal(true);
        setTitle(ResourceBundleHelper.getString("tag_add", ""));
        setMinimumSize(new Dimension(400, 400));

        addObserver(tagPanel);

        AbstractAction addTagAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTag();
                setVisible(false);
            }
        };
        tagPanel.addOnEnterAction(addTagAction);

        JButton okButton = new JButton("Ok");
        okButton.addActionListener(addTagAction);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(tagPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        add(panel);
        pack();
    }

    private void addTag() {
        List<Enterprise> enterprises = new ArrayList<>(demonstrator.getSelected());
        if (enterprises.isEmpty()) return;

        DAOTag daoTag = new DAOTag();
        daoTag.createTag(tagPanel.getCurrentTagTitle(), enterprises);
        notifyObservers(new ObservableEvent(ObservableEvent.TAGS_CHANGED));
    }



    public void updateDisplay() {
    }

    @Override
    public void addObserver(Observer observer) {
         observers.add(observer);
    }

    @Override
    public void notifyObservers(ObservableEvent event) {
        for (Observer observer : observers) {
            observer.update(event);
        }
    }

    @Override
    public void update(ObservableEvent event) {
        tagPanel.update(event);
    }
}
