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
        String allCheckBoxText = ResourceBundleHelper.getString("all_check_box_text", "All (%s) enterprises");
        JRadioButton allCheckBox = new JRadioButton(String.format(allCheckBoxText, allRowsCount));
        allCheckBox.addActionListener(new ChooseAction(ALL));

        String selectedOnlyCheckBoxText = ResourceBundleHelper.getString("selected_only_check_box_text", "Only selected (%s) enterprises");
        JRadioButton selectedCheckBox = new JRadioButton(String.format(selectedOnlyCheckBoxText, selectedRowsCount ));
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
