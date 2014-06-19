package md.varoinform.view.demonstrator;

import md.varoinform.controller.comparators.EnterpriseProxyComparator;
import md.varoinform.controller.entityproxy.EnterpriseProxy;

import javax.swing.*;
import java.util.*;

/**
* Created with IntelliJ IDEA.
* User: Vladimir Borovic
* Date: 6/17/14
* Time: 2:23 PM
*/
@SuppressWarnings("UnusedDeclaration")
class RowSorterWorker extends SwingWorker<List<EnterpriseProxy>, Void> {

    private EnterpriseTableModel enterpriseTableModel;
    public final List<EnterpriseProxy> proxy;
    private final int column;
    private final boolean asc;

    public RowSorterWorker(EnterpriseTableModel enterpriseTableModel, List<EnterpriseProxy> proxy, int column, SortingType type) {
        this.enterpriseTableModel = enterpriseTableModel;
        this.column = column;
        this.asc = type.type.equals(SortingType.asc.type);
        this.proxy = new ArrayList<>(proxy);
    }


    @Override
    protected List<EnterpriseProxy> doInBackground() throws Exception {
        Collections.sort(proxy, new EnterpriseProxyComparator(enterpriseTableModel.getColumnName(column), asc));
        return proxy;
    }

    public enum SortingType{
        asc("ascending"), desc("descending");
        private final String type;

        SortingType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type;
        }
    }

}
