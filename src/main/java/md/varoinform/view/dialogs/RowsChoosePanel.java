package md.varoinform.view.dialogs;

import md.varoinform.util.ResourceBundleHelper;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/28/13
 * Time: 3:11 PM
 */
public class RowsChoosePanel extends JPanel {
    public static final int SELECTED = 1;
    public static final int ALL = 2;
    private int choose = SELECTED;

    public RowsChoosePanel(String title, int selectedRowsCount, int allRowsCount) {
        String ent = ResourceBundleHelper.getString("enterprises", "enterprises");
        String all = ResourceBundleHelper.getString("all", "All");
        JRadioButton allCheckBox = new JRadioButton(all + " " + allRowsCount + " " + ent);
        allCheckBox.addActionListener(new ChooseAction(ALL));

        String selectedOnly = ResourceBundleHelper.getString("selected-only", "Only selected");
        JRadioButton selectedCheckBox = new JRadioButton(selectedOnly + " " + selectedRowsCount + " " + ent);
        selectedCheckBox.addActionListener(new ChooseAction(SELECTED));
        selectedCheckBox.setSelected(true);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(getTitledBorder(ResourceBundleHelper.getString(title, title)));

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(allCheckBox);
        buttonGroup.add(selectedCheckBox);

        add(selectedCheckBox);
        add(allCheckBox);


    }

    private TitledBorder getTitledBorder(String title) {
        return BorderFactory.createTitledBorder( title);
    }

    public int getChoose() {
        return choose;
    }

    private class ChooseAction implements ActionListener {
        private int value;

        public ChooseAction(int value) {
            this.value = value;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            choose = value;
        }
    }
}
