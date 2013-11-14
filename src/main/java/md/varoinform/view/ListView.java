package md.varoinform.view;

import md.varoinform.model.entities.Enterprise;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/14/13
 * Time: 1:07 PM
 */
public class ListView extends JList implements Demonstrator {
    private List<Enterprise> currentEnterprises;

    public ListView() {
        setCellRenderer(new EnterpriseCellRender());
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    @Override
    public void showResults(List<Enterprise> enterprises){
        currentEnterprises = enterprises;
        ModelCreator creator = new ModelCreator();
        Thread thread = new Thread(creator);
        thread.start();
    }

    @Override
    public List<Enterprise> getSelected(){
        //noinspection unchecked
        return getSelectedValuesList();
    }

    @Override
    public List<Enterprise> getALL() {
        return currentEnterprises;
    }

    @Override
    public void clear() {
        DefaultListModel model = (DefaultListModel)getModel();
        model.clear();
    }

    @Override
    public Enterprise getSelectedEnterprise() {
        return (Enterprise)getSelectedValue();
    }




    private class ModelCreator implements Runnable {

        @Override
        public void run() {
            getParent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            DefaultListModel model = new DefaultListModel();
            for (int i = 0; i < 5000; i++) {
                for (Enterprise enterprise : currentEnterprises) {
                    //noinspection unchecked
                    model.addElement(enterprise);
                }
            }

            setModel(model);

            getParent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
}
