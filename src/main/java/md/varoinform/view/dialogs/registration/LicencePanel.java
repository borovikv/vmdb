package md.varoinform.view.dialogs.registration;

import md.varoinform.Settings;
import md.varoinform.controller.DefaultLanguages;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.util.observer.ObservableEvent;
import md.varoinform.util.observer.ObservableIml;
import md.varoinform.util.observer.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/18/13
 * Time: 1:50 PM
 */
public class LicencePanel extends CardPanel implements md.varoinform.util.observer.Observable {
    private ObservableIml observableIml = new ObservableIml();
    private static final String CHECKBOX_KEY = "i_agree";
    private final JCheckBox licenceAgreeBox;
    private final JComboBox<DefaultLanguages> languageCombo;

    public LicencePanel() {
        licenceAgreeBox = new JCheckBox();
        licenceAgreeBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyObservers(new ObservableEvent(ObservableEvent.Type.IS_VALID));
            }
        });

        languageCombo = new  JComboBox<>(DefaultLanguages.values());
        languageCombo.setSelectedItem(DefaultLanguages.getDefault());
        languageCombo.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyObservers(new ObservableEvent<>(ObservableEvent.Type.LANGUAGE_CHANGED,
                        languageCombo.getSelectedItem()));
            }
        });

        createLayout();
    }

    public void createLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(new JScrollPane(label));
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(licenceAgreeBox, BorderLayout.WEST);
        panel.add(languageCombo, BorderLayout.EAST);
        add(panel, BorderLayout.SOUTH);
    }

    @Override
    public boolean isInputValid(){
        return licenceAgreeBox.isSelected();
    }

    @Override
    protected void updateDisplay() {
        licenceAgreeBox.setText(ResourceBundleHelper.getString(language, CHECKBOX_KEY, "i agree"));
        label.setText(getLicence());
    }

    private String getLicence() {
        Path path = Paths.get(Settings.getWorkFolder(), "external-resources", "licence",
                String.format("licence_%s.html", language.getTitle()));
        try {
            return new String(Files.readAllBytes(path), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void addObserver(Observer observer) {
        observableIml.addObserver(observer);
    }

    @Override
    public void notifyObservers(ObservableEvent event) {
        observableIml.notifyObservers(event);

    }
}
