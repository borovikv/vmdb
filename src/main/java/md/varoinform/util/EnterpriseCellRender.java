package md.varoinform.util;

import md.varoinform.model.entities.Enterprise;
import md.varoinform.view.EnterpriseView;

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
    private RenderTemplate renderer;

    public EnterpriseCellRender(RenderTemplate renderer) {
        super();
        this.renderer = renderer;
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Enterprise enterprise = (Enterprise)value;
        setText(EnterpriseView.getView(enterprise, renderer));
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
