package md.varoinform.view.dialogs;

import md.varoinform.util.PreferencesHelper;
import md.varoinform.view.I18nCheckBox;
import md.varoinform.view.MainFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 7/28/14
 * Time: 9:54 AM
 */
public class ShowTextButton extends I18nCheckBox {
    private final PreferencesHelper helper;
    public ShowTextButton(final MainFrame mainFrame) {
        super("show_text_in_button");

        helper = new PreferencesHelper();

        setSelected(helper.getShowTextInButton());
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                helper.setShowTextInButton(isSelected());
                mainFrame.updateDisplay();
            }
        });

    }
}
