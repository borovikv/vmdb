package md.varoinform.view.dialogs.registration;

import md.varoinform.sequrity.Registrar;
import md.varoinform.sequrity.RegistrationException;
import md.varoinform.util.ImageHelper;
import md.varoinform.util.ResourceBundleHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/14/13
 * Time: 12:06 PM
 */
public class RegistrationDialog extends JDialog implements Observer{

    private final Registrar registrar = new Registrar();
    private final RegisterByInternetPanel registerByInternetPanel = new RegisterByInternetPanel();
    private final RegisterByPhonePanel registerByPhonePanel = new RegisterByPhonePanel();
    private final JPanel card = new JPanel();
    private final List<CardPanel> cards = new ArrayList<>();
    private Integer currentCard = 0;

    private final JButton backButton;
    private final JButton nextButton;

    public RegistrationDialog() {
        setModal(true);
        int height = 600;
        int width = 600;
        setSize(width, height);
        setResizable(false);
        setLocationRelativeTo(null);
        setIconImage(ImageHelper.getImageIcon("/icons/V.png").getImage());

        nextButton = new JButton();
        nextButton.setEnabled(false);
        nextButton.addActionListener(new NextAction());

        registerByInternetPanel.setDocumentListener(nextButton);
        registerByPhonePanel.setDocumentListener(nextButton);

        LicencePanel licencePanel = new LicencePanel();
        licencePanel.addObserver(licencePanel);
        licencePanel.addObserver(registerByInternetPanel);
        licencePanel.addObserver(registerByPhonePanel);
        licencePanel.addObserver(this);
        licencePanel.addCheckBoxListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBox source = (JCheckBox) e.getSource();
                nextButton.setEnabled(source.isSelected());
            }
        });

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

        updateDisplay();
        add(card);
    }


    private void nextCard(){
        if (currentCard < 2){
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
        if (currentCard > 0){
            currentCard -= 1;
            move(currentCard);
        }
        if (currentCard == 0){
            backButton.setVisible(false);
            backButton.setEnabled(false);
        }
    }


    @Override
    public void update(Observable o, Object arg) {
        updateDisplay();
    }

    private void updateDisplay() {
        backButton.setText(TranslateHelper.instance.getText("back", "back"));
        nextButton.setText(TranslateHelper.instance.getText("next", "next"));
    }


    private class NextAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            CardPanel cardPanel = cards.get(currentCard);
            boolean valid = cardPanel.isInputValid();
            if (!valid) return;

            switch (currentCard){
                case 0:
                    nextCard();
                    break;

                case 1:
                    chooseRegistrationType();
                    break;

                case 2:
                    registerByPhone();
                    break;
            }

        }

        private void chooseRegistrationType() {

            switch (registerByInternetPanel.getRegisterType()){
                case RegisterByInternetPanel.INTERNET:
                    registerByInternet();
                    break;

                case RegisterByInternetPanel.PHONE:
                    setRegistrationCode();
                    nextCard();
                    break;
            }
        }

        private void registerByInternet() {
            String idDB = registerByInternetPanel.getIdDB();
            try {
                registrar.registerByInternet(idDB);
                setVisible(false);
            } catch (RegistrationException exception) {
                showExceptionMessage(exception);
            }
        }

        private void setRegistrationCode() {
            String idDB = registerByInternetPanel.getIdDB();
            String registrationCode = registrar.getRegistrationCode(idDB);
            registerByPhonePanel.setRegistrationCode(registrationCode);
        }


        private void registerByPhone() {
            String idDB = registerByInternetPanel.getIdDB();
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
            String message = ResourceBundleHelper.getString(exceptionMessage, exceptionMessage);
            JOptionPane.showMessageDialog(null, message);
        }

    }
}