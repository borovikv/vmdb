package md.varoinform.view.dialogs.progress;

import md.varoinform.Settings;
import md.varoinform.util.ResourceBundleHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
    private final SwingWorker<T, ?> activity;
    private JProgressBar progressBar;
    private JLabel messageLabel;
    private JButton cancelButton;
    private Timer cancelMonitor;
    private T result = null;

    private ActivityDialog(String message, final SwingWorker<T, ?> activity) {
        this.activity = activity;
        setModal(true);
        //setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setResizable(false);
        setTitle(ResourceBundleHelper.getString("activity-dialog-title"));
        setIconImage(Settings.getMainIcon());

        messageLabel = new JLabel(message);
        progressBar = new JProgressBar(0, 100);
        progressBar.setIndeterminate(true);
        cancelButton = new JButton(ResourceBundleHelper.getString("cancel", "Cancel"));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });
        createLayout();

        cancelMonitor = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (activity.isDone() && !activity.isCancelled()){
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

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cancel();
                super.windowClosing(e);
            }
        });
    }

    private void cancel(){
        activity.cancel(true);
        setVisible(false);
    }

    private void createLayout() {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addComponent(messageLabel)
                .addComponent(progressBar)
                .addComponent(cancelButton, GroupLayout.Alignment.CENTER)
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                        .addComponent(messageLabel)
                        .addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cancelButton)
        );


        panel.setLayout(layout);
        add(panel, BorderLayout.CENTER);
    }

    public static <T> T start(final SwingWorker<T, ?> activity, String message){
        activity.execute();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (activity.isDone()) {
            try {
                return activity.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        ActivityDialog<T> dialog = new ActivityDialog<>(message, activity);
        dialog.setVisible(true);
        dialog.dispose();
        return dialog.result;
    }
}
