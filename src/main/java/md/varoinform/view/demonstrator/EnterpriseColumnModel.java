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

        String translatedName = ResourceBundleHelper.getString(columnName, columnName);
        aColumn.setHeaderValue(translatedName);
    }
}
