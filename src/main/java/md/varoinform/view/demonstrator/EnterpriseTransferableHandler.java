package md.varoinform.view.demonstrator;

import md.varoinform.view.navigation.tags.TagList;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 5/6/14
 * Time: 5:05 PM
 */
public class EnterpriseTransferableHandler extends TransferHandler {
    @Override
    public int getSourceActions(JComponent c) {
        return TransferHandler.LINK;
    }


    @Override
    protected Transferable createTransferable(JComponent component) {
        TableView tableView = (TableView) component;
        List<Integer> selected = tableView.getSelected();
        if (selected.size() <= 0) return null;
        return new EnterpriseTransferable(selected);
    }

    @Override
    public boolean canImport(TransferSupport support) {
        return support.isDataFlavorSupported(EnterpriseTransferable.ENTERPRISE_FLAVOR) && (support.getComponent() instanceof JList);
    }

    @Override
    public boolean importData(JComponent comp, Transferable t) {
        TagList list = (TagList) comp;
        if(!t.isDataFlavorSupported(EnterpriseTransferable.ENTERPRISE_FLAVOR)) {
            return false;
        }
        try {
            @SuppressWarnings("unchecked")
            List<Integer> selected = (List<Integer>) t.getTransferData(EnterpriseTransferable.ENTERPRISE_FLAVOR);
            JList.DropLocation dropLocation = list.getDropLocation();
            Point point = dropLocation.getDropPoint();
            int index = list.locationToIndex(point);
            list.addTag(index, selected);
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
