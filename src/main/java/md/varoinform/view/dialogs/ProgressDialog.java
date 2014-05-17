package md.varoinform.view.dialogs;

import md.varoinform.view.dialogs.progress.Activity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 5/17/14
 * Time: 10:27 AM
 */
public class ProgressDialog extends JDialog{
    private static final int DEFAULT_HEIGHT = 150;
    private static final int DEFAULT_WIDTH = 350;
    private JProgressBar progressBar;
    private Timer cancelMonitor;
    private boolean isCanceled = false;
    private JLabel noteLabel;
    private JButton cancelButton;

    /**
     * Constructs a graphic object that shows progress, typically by filling
     * in a rectangular bar as the process nears completion.
     *
     * @param message         a descriptive message that will be shown
     *                        to the user to indicate what operation is being monitored.
     *                        This does not change as the operation progresses.
     *                        See the message parameters to methods in
     *                        {@link javax.swing.JOptionPane#message}
     *                        for the range of values.
     * @see javax.swing.JDialog
     * @see javax.swing.JOptionPane
     */
    private ProgressDialog(final Activity activity, String message) {
        super();
        setModal(true);
        setLocationRelativeTo(null);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setTitle(message);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                isCanceled = true;
            }
        });
        noteLabel = new JLabel("note");

        progressBar = new JProgressBar(0, 100);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isCanceled = true;
            }
        });

        createLayout();

        cancelMonitor = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isCanceled){
                    activity.cancel(true);
                }

                if (activity.isDone()){
                    close();
                } else {
                    progressBar.setValue(activity.getProgress());
                    String note = activity.getNote();
                    noteLabel.setText(note);
                }
            }
        });
        cancelMonitor.start();

    }

    private void createLayout() {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addComponent(noteLabel)
                .addComponent(progressBar));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(noteLabel)
                .addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));


        panel.setLayout(layout);


        add(panel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void close() {
        setVisible(false);
        dispose();
    }

    public static void start(Activity activity, String message){
        activity.execute();
        JDialog dialog = new ProgressDialog(activity, message);
        dialog.setVisible(true);
        dialog.dispose();
    }
}
