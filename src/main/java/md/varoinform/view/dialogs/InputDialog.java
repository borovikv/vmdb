package md.varoinform.view.dialogs;

import md.varoinform.Settings;
import md.varoinform.util.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 6/4/14
 * Time: 9:57 AM
 */
public class InputDialog extends JDialog {

    private static final int DEFAULT_HEIGHT = 200;
    private static final int DEFAULT_WIDTH = 400;
    private static final int MAX_WIDTH = 800;
    private JLabel messageLabel;
    private JTextField input;
    private JButton okButton;
    private Object value;

    public InputDialog(String message, Object value, Class<?> columnClass) {
        String text = StringUtils.valueOf(value);

        Font font = Settings.getDefaultFont(Settings.Fonts.SANS_SERIF);
        FontMetrics metrics = getFontMetrics(font);
        int messageWidth = metrics.stringWidth(message);
        int textWidth = metrics.stringWidth(text);
        int w = Math.min(MAX_WIDTH, Math.max(Math.max(messageWidth, DEFAULT_WIDTH), textWidth));
        setSize(w, DEFAULT_HEIGHT);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);

        messageLabel = new JLabel(message);

        ActionListener okAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (input instanceof JFormattedTextField) {
                    InputDialog.this.value = ((JFormattedTextField) input).getValue();
                } else {
                    InputDialog.this.value = input.getText().trim();
                }
                setVisible(false);
            }
        };

        input = getTextField(value, columnClass);
        input.addActionListener(okAction);

        okButton = new JButton("OK");
        okButton.addActionListener(okAction);
        createLayout();
    }

    public JTextField getTextField(Object value, Class<?> columnClass) {
        JTextField field;
        if (Date.class.isAssignableFrom(columnClass)){
            DateFormat defaultDateFormat = Settings.getDefaultDateFormat();
            field = new JFormattedTextField(defaultDateFormat);
            ((JFormattedTextField)field).setValue(value);
        } else if (Number.class.isAssignableFrom(columnClass)){
            field = new JFormattedTextField(NumberFormat.getNumberInstance());
            ((JFormattedTextField)field).setValue(value);
        } else {
            String text = StringUtils.valueOf(value);
            field = new JTextField(text);
        }


        return field;
    }


    private void createLayout() {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addComponent(messageLabel)
                .addComponent(input));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(messageLabel)
                .addComponent(input, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)) ;

        add(panel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public static Object showInputDialog(String message, Object value, Class<?> columnClass){
        InputDialog dialog = new InputDialog(message, value, columnClass);
        dialog.setVisible(true);
        return dialog.value;
    }

}
