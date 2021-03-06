package md.varoinform.view.dialogs.registration;

import md.varoinform.controller.DefaultLanguages;
import md.varoinform.sequrity.Registrar;
import md.varoinform.sequrity.exception.Error;
import md.varoinform.sequrity.exception.LockedException;
import md.varoinform.sequrity.exception.PasswordException;
import md.varoinform.sequrity.exception.RegistrationException;
import md.varoinform.util.ImageHelper;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.util.observer.ObservableEvent;
import md.varoinform.util.observer.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/14/13
 * Time: 12:06 PM
 */
public class RegistrationDialog extends JDialog implements Observer{

    private final Registrar registrar = new Registrar();
    private final RegisterByPhonePanel registerByPhonePanel = new RegisterByPhonePanel();
    private final RegisterByInternetPanel registerByInternetPanel = new RegisterByInternetPanel();
    private final JPanel card = new JPanel();
    private final List<CardPanel> cards = new ArrayList<>();
    private Integer currentCard = 0;

    private final JButton backButton;
    private final JButton nextButton;
    private final LicencePanel licencePanel;
    public static DefaultLanguages language;

    public RegistrationDialog() {
        setModal(true);
        int height = 600;
        int width = 600;
        setSize(width, height);
        setResizable(false);
        setLocationRelativeTo(null);
        setIconImage(ImageHelper.getImageIcon("/external-resources/icons/V.png").getImage());

        nextButton = new JButton();
        nextButton.setEnabled(false);
        nextButton.addActionListener(new NextAction());

        registerByPhonePanel.setDocumentListener(nextButton);
        registerByInternetPanel.setDocumentListener(nextButton);

        licencePanel = new LicencePanel();
        licencePanel.addObserver(licencePanel);
        licencePanel.addObserver(registerByPhonePanel);
        licencePanel.addObserver(registerByInternetPanel);
        licencePanel.addObserver(this);

        backButton = new JButton();
        backButton.setVisible(false);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previousCard();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        buttonPanel.add(nextButton);
        add(buttonPanel, BorderLayout.SOUTH);

        card.setLayout(new CardLayout());
        cards.add(licencePanel);
        cards.add(registerByInternetPanel);
        cards.add(registerByPhonePanel);

        for (int i = 0; i < cards.size(); i++) {
            card.add(cards.get(i), "" + i);
        }

        add(card);
        language = DefaultLanguages.getDefault();
        ObservableEvent event = new ObservableEvent<>(ObservableEvent.Type.LANGUAGE_CHANGED, language);
        licencePanel.notifyObservers(event);
    }


    private void nextCard(){
        if (currentCard < cards.size() - 1){
            currentCard++;
            move(currentCard);
        }
        if (currentCard > 0){
            backButton.setVisible(true);
            backButton.setEnabled(true);
            nextButton.setEnabled(cards.get(currentCard).isInputValid());
        }
    }

    private void move(Object name) {
        CardLayout cl = (CardLayout)(card.getLayout());
        cl.show(card, name.toString());
    }


    private void previousCard(){
        // if current card not first
        if (currentCard > 0){
            currentCard -= 1;
            move(currentCard);
        }
        if (currentCard == 0){
            backButton.setVisible(false);
            backButton.setEnabled(false);
        }
        nextButton.setEnabled(cards.get(currentCard).isInputValid());
    }


    @Override
    public void update(ObservableEvent event) {
        if (event.getType() == ObservableEvent.Type.LANGUAGE_CHANGED) {
            RegistrationDialog.language = (DefaultLanguages) event.getValue();
            updateDisplay();
        } else {
            nextButton.setEnabled(licencePanel.isInputValid());
        }
    }

    private void updateDisplay() {
        backButton.setText(ResourceBundleHelper.getString(language, "back", "back"));
        nextButton.setText(ResourceBundleHelper.getString(language, "next", "next"));
    }

    public static void register() {
        RegistrationDialog dialog = new RegistrationDialog();
        dialog.setVisible(true);
        dialog.dispose();
    }


    private class NextAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            CardPanel cardPanel = cards.get(currentCard);
            if (!cardPanel.isInputValid()) return;

            if (currentCard == 0){
                nextCard();
            } else if (currentCard == 1) {
                tryRegisterByInternet(getIdDB());
            } else {
                registerByPhone(getIdDB());
            }
        }

        private String getIdDB() {
            //return ResourceBundleHelper.getStringFromBundle(Settings.getConfigBundleKey(), "id");
            return registerByInternetPanel.getIdDB();
        }

        private void tryRegisterByInternet(String idDB) {
            try {
                registrar.registerByInternet(idDB);
                setVisible(false);
            } catch (RegistrationException exception) {
                Error error = exception.getError();
                if (error == null || error == Error.EXPIRED_UID || error == Error.INVALID_UID) {
                    showExceptionMessage(exception);
                    setVisible(false);
                    return;
                }
                registerByPhonePanel.setIdDB(getIdDB());
                nextCard();

            } catch (PasswordException | LockedException exception){
                showExceptionMessage(exception);
            }
        }

        private void registerByPhone(String idDB) {
            String password = registerByPhonePanel.getPassword();
            try {
                registrar.register(idDB, password);
                setVisible(false);
            } catch (RegistrationException | PasswordException | LockedException e) {
                showExceptionMessage(e);
            }
        }

        private void showExceptionMessage(Throwable exception) {
            String exceptionMessage = exception.getMessage();
            String message = ResourceBundleHelper.getString(language, exceptionMessage, exceptionMessage);
            JOptionPane.showMessageDialog(null, message);
        }

    }

    public static void main(String[] args) {
        RegistrationDialog dialog = new RegistrationDialog();
        dialog.setVisible(true);
        dialog.dispose();
    }
}
