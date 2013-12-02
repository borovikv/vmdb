package md.varoinform.view.dialogs;

import md.varoinform.model.dao.DAOTag;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.entities.Tag;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.view.TagPanel;
import md.varoinform.view.demonstrator.Demonstrator;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/29/13
 * Time: 9:56 AM
 */
public class TagDialog extends JDialog {
    private final Demonstrator demonstrator;
    private final TagPanel tagPanel;
    private final TagPanel observer;


    public TagDialog(Component parent, Demonstrator demonstrator, TagPanel observer) {
        this.demonstrator = demonstrator;
        this.observer = observer;
        setLocationRelativeTo(parent);
        setModal(true);
        setTitle(ResourceBundleHelper.getString("tag_add", ""));
        setMinimumSize(new Dimension(400, 400));

        JPanel panel = new JPanel(new BorderLayout());

        tagPanel = new TagPanel();
        panel.add(tagPanel, BorderLayout.CENTER);

        tagPanel.addSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                @SuppressWarnings("unchecked")
                JList<Tag> list = (JList<Tag>) e.getSource();
                Tag selectedValue = list.getSelectedValue();
                if (selectedValue != null)
                    tagPanel.setText(selectedValue.getTitle());
            }
        });

        AbstractAction addTagAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTag();
                setVisible(false);
            }
        };
        tagPanel.addOnEnterAction(addTagAction);

        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("Ok");
        okButton.addActionListener(addTagAction);
        buttonPanel.add(okButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
        pack();
    }

    private void addTag() {
        String tag = tagPanel.getCurrentTag();
        List<Enterprise> enterprises = demonstrator.getSelected();
        if (enterprises.isEmpty()) return;

        DAOTag daoTag = new DAOTag();
        daoTag.createTag(tag, enterprises);
        tagPanel.fireTagsChanged();
        observer.fireTagsChanged();
    }


    public void updateDisplay() {
    }
}
