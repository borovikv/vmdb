package md.varoinform.view.dialogs.preview;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.*;

/**
 * Created with IntelliJ IDEA.
 * User: vb
 * Date: 11/21/13
 * Time: 2:17 PM
 */
public class BookTestFrame extends JFrame {
    private final JTextField text;
    private final HashPrintRequestAttributeSet attributes;
    private PageFormat pageFormat;

    public BookTestFrame() throws HeadlessException {
        setTitle("BookTest");
        text = new JTextField();
        add(text, BorderLayout.NORTH);

        attributes = new HashPrintRequestAttributeSet();

        JPanel buttonPanel = new JPanel();

        JButton printButton = new JButton("Print");
        buttonPanel.add(printButton);
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PrinterJob job = PrinterJob.getPrinterJob();
                    job.setPageable(makeBook());
                    if (job.printDialog(attributes)) {
                        job.print(attributes);
                    }
                } catch (PrinterException e1) {
                    e1.printStackTrace();
                }
            }
        });

        JButton pageSetupButton = new JButton("Page setup");
        buttonPanel.add(pageSetupButton);
        pageSetupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PrinterJob job = PrinterJob.getPrinterJob();
                pageFormat = job.pageDialog(attributes);
            }
        });

        JButton printPreviewButton = new JButton("Print preview");
        buttonPanel.add(printPreviewButton);
        printPreviewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PrintPreviewDialog dialog = new PrintPreviewDialog(makeBook());
                dialog.setVisible(true);
            }
        });

        add(buttonPanel, BorderLayout.SOUTH);
        pack();
    }

    private Book makeBook() {
        if (pageFormat == null){
            PrinterJob job = PrinterJob.getPrinterJob();
            pageFormat = job.defaultPage();
        }

        Book book = new Book();
        String message = text.getText();
        Banner banner = new Banner(message);
        int pageCount = banner.getPageCount((Graphics2D)getGraphics(), pageFormat);
        //book.append(new CoverPage(message), pageFormat);
        book.append(new Mark(), pageFormat);
        book.append(banner, pageFormat, pageCount);
        return book;
    }
}
