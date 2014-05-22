package md.varoinform.view.dialogs.export;

import md.varoinform.model.entities.Enterprise;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.view.demonstrator.Demonstrator;
import md.varoinform.view.dialogs.FieldChoosePanel;
import md.varoinform.view.dialogs.progress.ProgressDialog;
import md.varoinform.view.dialogs.RowsChoosePanel;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/28/13
 * Time: 2:57 PM
 */
public class ExportDialog extends JDialog {

    private Demonstrator demonstrator;
    private FieldChoosePanel fieldChooser = new FieldChoosePanel();
    private RowsChoosePanel rowsChoosePanel;
    private final JButton saveButton;

    public ExportDialog(Demonstrator demonstrator) {
        this.demonstrator = demonstrator;

        setSize(500, 500);
        setModal(true);
        setLocationRelativeTo(null);

        rowsChoosePanel = new RowsChoosePanel("export-rows-choose", demonstrator.getSelected().size(), demonstrator.getALL().size());
        add(rowsChoosePanel, BorderLayout.WEST);

        JScrollPane scrollPane = new JScrollPane(fieldChooser);
        scrollPane.setBorder(getTitledBorder(ResourceBundleHelper.getString("fields", "Fields")));
        add(scrollPane, BorderLayout.CENTER);

        saveButton = new JButton("Ok");
        saveButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save(fieldChooser.getSelectedFieldNames(), getEnterprises());
                setVisible(false);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);

    }

    private TitledBorder getTitledBorder(String title) {
        return BorderFactory.createTitledBorder( title);
    }


    private void save(List<String> fieldNames, List<Enterprise> enterprises){

        JFileChooser saveDialog = new JFileChooser(System.getProperty("user.home"));
        FileFilter csvFileFilter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                String extension = FilenameUtils.getExtension(f.getAbsolutePath());
                return f.isDirectory() || extension.equalsIgnoreCase("csv");
            }

            @Override
            public String getDescription() {
                return "csv";
            }
        };
        saveDialog.addChoosableFileFilter(csvFileFilter);
        saveDialog.setFileFilter(csvFileFilter);

        if (JFileChooser.APPROVE_OPTION == saveDialog.showSaveDialog(this)){
            File file = saveDialog.getSelectedFile();
            if (!file.getName().endsWith(".csv")){
                file = new File(file.getAbsolutePath() + ".csv");
            }
            if (enterprises.isEmpty()) return;

            ExportActivity exportActivity = new ExportActivity(file, fieldNames, enterprises);
            ProgressDialog.start(exportActivity, ResourceBundleHelper.getString("wait_for_export", "wait"));
        }
    }

    private List<Enterprise> getEnterprises() {
        if (rowsChoosePanel.getChoose() == RowsChoosePanel.ALL){
            return demonstrator.getALL();
        }
        return demonstrator.getSelected();
    }


    public void updateDisplay() {
        fieldChooser.updateDisplay();
    }

    public static void export(Demonstrator demonstrator){
        final ExportDialog exportDialog = new ExportDialog(demonstrator);
        exportDialog.setVisible(true);
    }
}
