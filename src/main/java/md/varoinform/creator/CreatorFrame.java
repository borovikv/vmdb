package md.varoinform.creator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 3/19/14
 * Time: 10:16 AM
 */
public class CreatorFrame extends JFrame{

    private static final int DEFAULT_WIDTH = 600;
    private static final int DEFAULT_HEIGHT = 300;
    private final String description = "Выберите папку с файлами для импорта затем нажмите кнопку \"Создать\"";
    private File selectedFile;
    private final JTextField folderField = new JTextField();

    public static void main(String[] args) {
        CreatorFrame creatorFrame = new CreatorFrame();
        creatorFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        creatorFrame.setVisible(true);
    }
    public CreatorFrame() {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);

        JTextArea descriptionLabel = new JTextArea(description);
        descriptionLabel.setLineWrap(true);
        descriptionLabel.setEditable(false);

        folderField.setBorder(BorderFactory.createCompoundBorder(
                folderField.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        JButton chooseFolderButton = new JButton("Выбрать папку");
        chooseFolderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);
                if (chooser.showOpenDialog(CreatorFrame.this) == JFileChooser.APPROVE_OPTION) {
                    selectedFile = chooser.getSelectedFile();
                    folderField.setText(selectedFile.getAbsolutePath());
                }
            }
        });

        JButton okButton = new JButton("Создать");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Creator creator = new Creator(selectedFile);
                try {
                    File file = creator.create();
                    sendToServer(file);
                    JOptionPane.showMessageDialog(CreatorFrame.this, "Создание базы успешно завершено");
                    System.exit(0);
                } catch (CreateException | SenderException e1) {
                    JOptionPane.showMessageDialog(CreatorFrame.this, e1.getMessage());
                }
            }
        });


        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addComponent(descriptionLabel)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(folderField)
                        .addComponent(chooseFolderButton))
                .addComponent(okButton)
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(descriptionLabel)
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup()
                        .addComponent(folderField, GroupLayout.Alignment.CENTER, 0, GroupLayout.DEFAULT_SIZE, folderField.getPreferredSize().height)
                        .addComponent(chooseFolderButton))
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(okButton)
        );


        add(panel, BorderLayout.CENTER);

    }

    private void sendToServer(File file) throws SenderException {
        new Sender().send(file);

    }


}
