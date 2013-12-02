package md.varoinform.view.demonstrator;

import md.varoinform.controller.entityproxy.EnterpriseProxy;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.util.PreferencesHelper;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/14/13
 * Time: 4:47 PM
 */
public class EnterpriseTableModel extends AbstractTableModel {

    private List<Enterprise> enterprises;
    private final PreferencesHelper preferencesHelper = new PreferencesHelper();

    public EnterpriseTableModel() {
        enterprises = new ArrayList<>();
    }

    public EnterpriseTableModel(List<Enterprise> enterprises) {
        this.enterprises = enterprises;
    }


    @Override
    public int getRowCount() {
        return enterprises.size();
    }


    @Override
    public int getColumnCount() {
        return getColumns().size();
    }

    private List<String> getColumns() {
        String prefColumns = preferencesHelper.getPrefColumns();

        List<String> result =  new ArrayList<>();
        Collections.addAll(result, prefColumns.split(";"));
        return result;
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        EnterpriseProxy proxy = new EnterpriseProxy( enterprises.get(rowIndex) );
        List<String> columns = getColumns();
        String name = columns.get(columnIndex);
        return proxy.get(name);
    }


    @Override
    public String getColumnName(int column) {
        String columnName = "";
        if (column < getColumns().size())
            columnName = getColumns().get( column );

        return columnName;
    }

    public List<Enterprise> getEnterprises() {
        return enterprises;
    }

    public Enterprise getEnterpriseAt(int rowIndex) {
        if ( 0 > rowIndex || rowIndex > enterprises.size() ) return null;
        return enterprises.get(rowIndex);
    }
}