package md.varoinform.view;

import md.varoinform.model.entities.Enterprise;
import md.varoinform.controller.EnterpriseView;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/1/13
 * Time: 10:59 AM
 */
public class EnterpriseCellRender extends JLabel implements ListCellRenderer {
    private static final Color HIGHLIGHT_COLOR = new Color(255, 255, 204);
    private static final Color ALTERNATIVE_COLOR = new Color(238, 238, 238);

    public EnterpriseCellRender() {
        super();
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Enterprise enterprise = (Enterprise)value;
        setText(EnterpriseView.getCellView(enterprise));
        if (index%2 == 0 && !isSelected){
            setBackground(ALTERNATIVE_COLOR);
        } else if (index%2 != 0 && !isSelected){
            setBackground(Color.WHITE);
        } else if (isSelected) {
            setBackground(HIGHLIGHT_COLOR);
        } else {
            setBackground(Color.white);
        }
        return this;
    }



}
