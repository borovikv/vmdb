package md.varoinform.view.dialogs.preview;

import md.varoinform.util.ImageHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.Book;

/**
 * Created with IntelliJ IDEA.
 * User: vb
 * Date: 11/21/13
 * Time: 3:42 PM
 */
public class PrintPreviewDialog extends JDialog{
    private PrintPreviewCanvas canvas;
    private JLabel pageLabel = new JLabel();
    private Book book;

    public PrintPreviewDialog(Book book) {
        this.book = book;
        layoutUI(book);
    }

    private void layoutUI(Book book) {
        ImageIcon image = ImageHelper.getImageIcon("/icons/V.png");
        setIconImage(image.getImage());
        setModal(true);
        setDefaultLookAndFeelDecorated(true);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setMinimumSize(new Dimension(400, 300));

        canvas = new PrintPreviewCanvas(book);
        add(canvas, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEtchedBorder());

        addButton(buttonPanel, "Previous");
        addButton(buttonPanel, "Next");
        //addButton(buttonPanel, "Close");

        buttonPanel.add(pageLabel);
        pageLabel.setBorder(BorderFactory.createEtchedBorder());
        setPage();

        add(buttonPanel, BorderLayout.NORTH);
    }

    private void addButton(JPanel buttonPanel, final String title) {
        JButton button = new JButton(title);
        buttonPanel.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (title) {
                    case "Next":
                        canvas.flipPage(1);
                        setPage();
                        break;
                    case "Previous":
                        canvas.flipPage(-1);
                        setPage();
                        break;
                    case "Close":
                        setVisible(false);
                        break;
                }
            }
        });
    }

    private void setPage() {
        pageLabel.setText("Page " + (canvas.getPage() + 1) + " of " + book.getNumberOfPages());
    }
}
