package md.varoinform.view.demonstrator;

import md.varoinform.controller.entityproxy.EnterpriseProxy;
import md.varoinform.util.StringUtils;

import javax.swing.*;
import java.util.*;

/**
* Created with IntelliJ IDEA.
* User: Vladimir Borovic
* Date: 6/17/14
* Time: 2:23 PM
*/
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
        Collections.sort(proxy, new Comparator<EnterpriseProxy>() {
            @Override
            public int compare(EnterpriseProxy o1, EnterpriseProxy o2) {
                String name = enterpriseTableModel.getColumnName(column);
                Object o3 = o1.get(name);
                Object o4 = o2.get(name);
                if (o3 == null && o4 == null) return 0;
                if ((o3 == null && asc) || (o4 == null && !asc)) return -1;
                if (o3 == null || o4 == null) return 1;

                Class<?> cls = enterpriseTableModel.getColumnClass(column);
                if (Date.class.isAssignableFrom(cls) || Number.class.isAssignableFrom(cls)) {
                    if (asc) {
                        //noinspection unchecked
                        return ((Comparable) o3).compareTo(o4);
                    } else {
                        //noinspection unchecked
                        return -((Comparable) o3).compareTo(o4);
                    }
                } else if (asc) {
                    return StringUtils.valueOf(o3).compareTo(StringUtils.valueOf(o4));
                } else {
                    return -StringUtils.valueOf(o3).compareTo(StringUtils.valueOf(o4));
                }
            }
        });
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
