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
    private static final String REGISTRATION_TYPE_KEY = "registration";
    private static final String CHECKBOX_KEY = "i_agree";

    private ObservableIml observableIml = new ObservableIml();

    private final JCheckBox licenceAgreeBox;
    private final JComboBox<DefaultLanguages> languageCombo;

    private RegistrationType type;
    private final JRadioButton phoneButton;
    private final JRadioButton internetButton;
    private final JLabel registrLabel;

    public LicencePanel() {
        type = RegistrationType.INTERNET;

        registrLabel = new JLabel();
        internetButton = new JRadioButton();
        internetButton.setSelected(true);
        internetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type = RegistrationType.INTERNET;
            }
        });
        phoneButton = new JRadioButton();
        phoneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type = RegistrationType.PHONE;
            }
        });
        ButtonGroup group = new ButtonGroup();
        group.add(internetButton);
        group.add(phoneButton);

        licenceAgreeBox = new JCheckBox();
        licenceAgreeBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyObservers(new ObservableEvent(ObservableEvent.Type.IS_VALID));
            }
        });


        languageCombo = new  JComboBox<>(DefaultLanguages.values());
        languageCombo.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyObservers(new ObservableEvent(ObservableEvent.Type.LANGUAGE_CHANGED,
                        languageCombo.getSelectedItem()));
            }
        });

        createLayout();
    }

    public void createLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JPanel typePanel = new JPanel();
        typePanel.add(registrLabel);
        typePanel.add(internetButton);
        typePanel.add(phoneButton);
        add(typePanel, BorderLayout.NORTH);
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
        internetButton.setText(ResourceBundleHelper.getString(language, RegistrationType.INTERNET.getTitle(), "internet"));
        phoneButton.setText(ResourceBundleHelper.getString(language, RegistrationType.PHONE.getTitle(), "phone"));
        licenceAgreeBox.setText(ResourceBundleHelper.getString(language, CHECKBOX_KEY, "i agree"));
        registrLabel.setText(ResourceBundleHelper.getString(language, REGISTRATION_TYPE_KEY, "register by"));
        label.setText(getLicence());
    }

    private String getLicence() {
        Path path = Paths.get(Settings.getWorkFolder(), "external-resources", "licence",
                String.format("licence_%s.html", language.getTitle()));
        try {
            return new String(Files.readAllBytes(path));
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

    public DefaultLanguages getCurrentLanguage(){
        return (DefaultLanguages) languageCombo.getSelectedItem();
    }

    public RegistrationType getType() {
        return type;
    }
}
