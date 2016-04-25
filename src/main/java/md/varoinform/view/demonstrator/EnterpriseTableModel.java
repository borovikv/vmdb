package md.varoinform.view.demonstrator;

import md.varoinform.model.dao.EnterpriseDao;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.util.PreferencesHelper;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/14/13
 * Time: 4:47 PM
 */
public class EnterpriseTableModel extends AbstractTableModel {
    private List<Integer> enterprises;
    private final PreferencesHelper preferencesHelper = new PreferencesHelper();

    public EnterpriseTableModel() {
        enterprises = new ArrayList<>();
    }

    public EnterpriseTableModel(List<Integer> enterprises) {
        System.out.println(enterprises);
        this.enterprises = new ArrayList<>(enterprises);
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
        return preferencesHelper.getUserFields();
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String name = getColumnName(columnIndex);
        Enterprise e = EnterpriseDao.getEnterprise(enterprises.get(rowIndex));
        return e.getValue(name);
    }

    @Override
    public String getColumnName(int column) {
        String columnName = "";
        List<String> columns = getColumns();
        if (column < columns.size())
            columnName = columns.get( column );

        return columnName;
    }

    public List<Integer> getEnterprises() {
        return enterprises;
    }

    public Integer getEnterpriseAt(int rowIndex) {
        if ( 0 > rowIndex || rowIndex > enterprises.size() ) return null;
        return enterprises.get(rowIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        String name = getColumnName(columnIndex);
        return Enterprise.getColumnType(name);
    }
}
