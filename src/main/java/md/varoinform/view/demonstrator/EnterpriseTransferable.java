package md.varoinform.view.demonstrator;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 5/6/14
 * Time: 5:18 PM
 */
public class EnterpriseTransferable implements Transferable {
    private final List<Integer> enterprises;
    public final static DataFlavor ENTERPRISE_FLAVOR = new DataFlavor(Object.class, "Enterprise list");

    public EnterpriseTransferable(List<Integer> enterprises) {
        this.enterprises = enterprises;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{ENTERPRISE_FLAVOR};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor == ENTERPRISE_FLAVOR;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        return enterprises;
    }
}
