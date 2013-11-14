package md.varoinform.view;

import md.varoinform.controller.ContactProxy;
import md.varoinform.controller.EnterpriseProxy;
import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.entities.Enterprise;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/14/13
 * Time: 1:05 PM
 */
public class TableView extends JTable implements Demonstrator {
    private List<Enterprise> currentEnterprises;
    private Object[] columnNames;

    public TableView() {
        super();
        setRowHeight(20);
        setColumnNames();
        setModel(new DefaultTableModel(columnNames, 0));

    }

    public void addListSelectionListener(ListSelectionListener listener){
        ListSelectionModel rowSM = getSelectionModel();
        rowSM.addListSelectionListener(listener);
    }

    private void setColumnNames() {
        Locale locale = new Locale(LanguageProxy.getInstance().getCurrentLanguageTitle());
        ResourceBundle bundle = ResourceBundle.getBundle("i18n.Strings", locale);
        columnNames = new Object[]{
                bundle.getString("title"), bundle.getString("address"), bundle.getString("phone"),
                bundle.getString("fax"), bundle.getString("email"), bundle.getString("urls"), "sssssss" };
    }

    @Override
    public void showResults(List<Enterprise> enterprises) {
        if (enterprises == null) return;
        currentEnterprises = enterprises;

        //DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        EnterpriseTableModel model = new EnterpriseTableModel(columnNames, 0);
        for (int i = 0; i < enterprises.size() * 5000; i++) {
            EnterpriseProxy proxy = new EnterpriseProxy(enterprises.get(i%enterprises.size()));
            ContactProxy cp = proxy.getContactProxies().get(0);
            model.addRow(new Object[]{proxy.getTitle(), cp.getAddress(), cp.getPhones(), cp.getFax(), cp.getEmail(), cp.getUrls(), proxy.getBranches()});
        }
        setModel(model);
        doLayout();
    }

    @Override
    public List<Enterprise> getSelected() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Enterprise> getALL() {
        return currentEnterprises;
    }

    @Override
    public void clear() {
        ((DefaultTableModel)getModel()).setRowCount(0);
    }

    @Override
    public Enterprise getSelectedEnterprise() {
        if(currentEnterprises == null) return null;
        int index = getSelectedRow() % currentEnterprises.size();
        if ( index < 0 || index >= currentEnterprises.size()) return null;
        return currentEnterprises.get(index);
    }

    @Override
    public void updateUI() {
        super.updateUI();    //To change body of overridden methods use File | Settings | File Templates.
        setColumnNames();
        showResults(currentEnterprises);

    }
}
