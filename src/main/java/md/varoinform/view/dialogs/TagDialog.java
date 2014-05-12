package md.varoinform.view.dialogs;

import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.view.navigation.tags.TagPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/29/13
 * Time: 9:56 AM
 */
public class TagDialog extends JDialog {
    private final TagPanel tagPanel = new TagPanel();
    private static String tagTitle = "";


    private TagDialog() {
        setLocationRelativeTo(null);
        setModal(true);
        setTitle(ResourceBundleHelper.getString("tag_add", ""));
        setMinimumSize(new Dimension(400, 400));

        AbstractAction addTagAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tagTitle = tagPanel.getCurrentTagTitle();
                setVisible(false);
                tagPanel.clearFilter();
            }
        };
        tagPanel.addOnEnterAction(addTagAction);

        JButton okButton = new JButton("Ok");
        okButton.addActionListener(addTagAction);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(tagPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        add(panel);
        pack();
    }


    public static String getTag() {
        TagDialog tagDialog = new TagDialog();
        tagDialog.setVisible(true);
        return tagTitle;
    }
}
