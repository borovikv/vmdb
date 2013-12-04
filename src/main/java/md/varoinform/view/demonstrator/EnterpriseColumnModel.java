package md.varoinform.view.demonstrator;

import md.varoinform.util.ResourceBundleHelper;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/18/13
 * Time: 5:14 PM
 */
public class EnterpriseColumnModel extends DefaultTableColumnModel {

    @Override
    public void addColumn(TableColumn aColumn) {
        super.addColumn(aColumn);

        String columnName = (String)aColumn.getHeaderValue();
        aColumn.setIdentifier(columnName);

        setColumnHeader(aColumn, columnName);
    }

    private void setColumnHeader(TableColumn aColumn, String columnName) {
        String translatedName = ResourceBundleHelper.getString(columnName, columnName);
        aColumn.setHeaderValue(translatedName);
    }

    public void updateHeader(){
        for (int i = 0; i < getColumnCount(); i++) {
            TableColumn aColumn = getColumn(i);
            String identifier = (String) aColumn.getIdentifier();
            setColumnHeader(aColumn, identifier);
        }
    }
}
