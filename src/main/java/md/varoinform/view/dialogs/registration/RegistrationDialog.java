package md.varoinform.view.dialogs.registration;

import md.varoinform.Settings;
import md.varoinform.sequrity.Registrar;
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
    private final JPanel card = new JPanel();
    private final List<CardPanel> cards = new ArrayList<>();
    private Integer currentCard = 0;

    private final JButton backButton;
    private final JButton nextButton;
    private final LicencePanel licencePanel;

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

        licencePanel = new LicencePanel();
        licencePanel.addObserver(licencePanel);
        licencePanel.addObserver(registerByPhonePanel);
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
        cards.add(registerByPhonePanel);
        for (int i = 0; i < cards.size(); i++) {
            card.add(cards.get(i), "" + i);
        }

        add(card);
        licencePanel.notifyObservers(new ObservableEvent(ObservableEvent.Type.LANGUAGE_CHANGED, licencePanel.getCurrentLanguage()));
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
            updateDisplay();
        } else {
            nextButton.setEnabled(licencePanel.isInputValid());
        }
    }

    private void updateDisplay() {
        backButton.setText(ResourceBundleHelper.getString(licencePanel.getCurrentLanguage(), "back", "back"));
        nextButton.setText(ResourceBundleHelper.getString(licencePanel.getCurrentLanguage(), "next", "next"));
    }


    private class NextAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            CardPanel cardPanel = cards.get(currentCard);
            if (!cardPanel.isInputValid()) return;

            String idDB = ResourceBundleHelper.getStringFromBundle(Settings.getConfigBundleKey(), "id");
            if (licencePanel.getType() == RegistrationType.INTERNET){
                try {
                    tryRegisterByInternet(idDB);
                } catch (RegistrationException rex){
                    nextCard();
                }

            } else if (licencePanel.getType() == RegistrationType.PHONE && currentCard == 0){
                nextCard();
            } else if (licencePanel.getType() == RegistrationType.PHONE){
                registerByPhone(idDB);
            }
        }

        private void tryRegisterByInternet(String idDB) throws RegistrationException {
            try {
                registrar.registerByInternet(idDB);
                setVisible(false);
            } catch (RegistrationException exception) {
                String text;
                if (exception.getError() == RegistrationException.Error.CONNECTION_ERROR){
                    text = ResourceBundleHelper.getString(licencePanel.getCurrentLanguage(), "request_error_message", "");
                } else if (exception.getError() == RegistrationException.Error.RESPONSE_ERROR){
                    text = ResourceBundleHelper.getString(licencePanel.getCurrentLanguage(), "response_error_message", "");
                } else {
                    showExceptionMessage(exception);
                    setVisible(false);
                    return;
                }

                if (JOptionPane.showConfirmDialog(null, text) == JOptionPane.NO_OPTION){
                    setVisible(false);
                }
            }
        }

        private void registerByPhone(String idDB) {
            String password = registerByPhonePanel.getPassword();
            try {
                registrar.registerByPhone(idDB, password);
                setVisible(false);
            } catch (RegistrationException e) {
                showExceptionMessage(e);
            }
        }

        private void showExceptionMessage(RegistrationException exception) {
            exception.printStackTrace();
            String exceptionMessage = exception.getMessage();
            String message = ResourceBundleHelper.getString(licencePanel.getCurrentLanguage(), exceptionMessage, exceptionMessage);
            JOptionPane.showMessageDialog(null, message);
        }

    }

    public static void main(String[] args) {
        RegistrationDialog dialog = new RegistrationDialog();
        dialog.setVisible(true);
        dialog.dispose();
    }
}
