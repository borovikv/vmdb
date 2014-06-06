package md.varoinform.view.dialogs.progress;

import md.varoinform.Settings;
import md.varoinform.util.ResourceBundleHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 6/5/14
 * Time: 5:14 PM
 */
public class ActivityDialog<T> extends JDialog {
    private static final int DEFAULT_HEIGHT = 150;
    private static final int DEFAULT_WIDTH = 350;
    private JProgressBar progressBar;
    private JLabel messageLabel;
    private Timer cancelMonitor;
    private T result = null;

    private ActivityDialog(String message, final SwingWorker<T, ?> activity) {
        activity.execute();

        setModal(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setResizable(false);
        setTitle(ResourceBundleHelper.getString("activity-dialog-title"));
        setIconImage(Settings.getMainIcon());

        messageLabel = new JLabel(message);
        progressBar = new JProgressBar(0, 100);
        progressBar.setIndeterminate(true);
        createLayout();

        cancelMonitor = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (activity.isDone()){
                    try {
                        result = activity.get();
                    } catch (InterruptedException | ExecutionException e1) {
                        e1.printStackTrace();
                    }
                    setVisible(false);
                } else {
                    progressBar.setValue(10);
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
                .addComponent(messageLabel)
                .addComponent(progressBar));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(messageLabel)
                .addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));


        panel.setLayout(layout);
        add(panel, BorderLayout.CENTER);
    }

    public static <T> T start(SwingWorker<T, ?> activity, String message){
        ActivityDialog<T> dialog = new ActivityDialog<>(message, activity);
        dialog.setVisible(true);
        dialog.dispose();
        return dialog.result;
    }
}
