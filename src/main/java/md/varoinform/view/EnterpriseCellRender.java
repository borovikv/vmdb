package md.varoinform.view;

import md.varoinform.model.entities.Enterprise;
import md.varoinform.controller.EnterpriseView;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/1/13
 * Time: 10:59 AM
 */
public class EnterpriseCellRender extends JPanel implements ListCellRenderer {
    private static final Color HIGHLIGHT_COLOR;
    private static final Color ALTERNATIVE_COLOR;
    static {
        ResourceBundle bundle = ResourceBundle.getBundle("VaroDB");
        HIGHLIGHT_COLOR = (Color)bundle.getObject("highlightColor");
        ALTERNATIVE_COLOR = (Color)bundle.getObject("alternativeColor");
    }

    private JLabel labelNumber = new JLabel();
    private JLabel labelText = new JLabel();

    public EnterpriseCellRender() {
        super();

        setLayout(new BorderLayout());

        labelNumber.setPreferredSize(new Dimension(50, 0));
        labelNumber.setBorder(BorderFactory.createEmptyBorder(0, 5, 0,  5));
        labelNumber.setHorizontalAlignment(SwingConstants.RIGHT);
        add(labelNumber, BorderLayout.WEST);

        add(labelText);
        setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Enterprise enterprise = (Enterprise)value;
        labelNumber.setText("" + (index + 1));
        //labelText.setText(enterprise.getTitles().get(0).getTitle());
        long start = System.nanoTime();
        String text = EnterpriseView.getCellView(enterprise);
        long end = System.nanoTime();
        double sec = (double)(end - start) / 1000000000.0;
        //System.out.println(sec*15000);
        labelText.setText(text);

        if (index%2 == 0 && !isSelected){
            setBackground(ALTERNATIVE_COLOR);

        } else if (index%2 != 0 && !isSelected){
            setBackground(Color.WHITE);

        } else if (isSelected) {
            setBackground(HIGHLIGHT_COLOR);

        } else {
            setBackground(Color.WHITE);

        }

        return this;
    }



}
