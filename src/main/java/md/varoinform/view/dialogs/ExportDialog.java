package md.varoinform.view.dialogs;

import au.com.bytecode.opencsv.CSVWriter;
import md.varoinform.controller.comparators.ColumnPriorityComparator;
import md.varoinform.controller.entityproxy.EnterpriseProxy;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.util.StringUtils;
import md.varoinform.view.demonstrator.Demonstrator;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
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

    public ExportDialog(Demonstrator demonstrator) {
        this.demonstrator = demonstrator;

        setSize(500, 500);
        setModal(true);
        setLocationRelativeTo(null);

        rowsChoosePanel = new RowsChoosePanel("export rows", demonstrator.getSelected().size(), demonstrator.getALL().size());
        add(rowsChoosePanel, BorderLayout.WEST);

        JScrollPane scrollPane = new JScrollPane(fieldChooser);
        scrollPane.setBorder(getTitledBorder(ResourceBundleHelper.getString("fields", "Fields")));
        add(scrollPane, BorderLayout.CENTER);

        JButton saveButton = new JButton("Ok");
        saveButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);

    }

    private TitledBorder getTitledBorder(String title) {
        return BorderFactory.createTitledBorder( title);
    }


    private void save(){
        JFileChooser saveDialog = new JFileChooser(System.getProperty("user.home"));
        if (JFileChooser.APPROVE_OPTION == saveDialog.showSaveDialog(this)){
            save(saveDialog.getSelectedFile());
        }
    }

    private void save(File selectedFile) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(selectedFile), ';');
            List<String> selectedColumns = fieldChooser.getSelectedFieldNames();
            Collections.sort(selectedColumns, new ColumnPriorityComparator());
            writer.writeNext(selectedColumns.toArray(new String[selectedColumns.size()]));
            for (Enterprise enterprise : getEnterprises()) {
                writeLine(writer, selectedColumns, enterprise);
            }

            writer.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void writeLine(CSVWriter writer, List<String> selectedColumns, Enterprise enterprise) {
        String[] entries = new String[selectedColumns.size()];
        EnterpriseProxy proxy = new EnterpriseProxy(enterprise);
        for (int i = 0; i < selectedColumns.size(); i++) {
            Object obj = proxy.get(selectedColumns.get(i));
            entries[i] = StringUtils.valueOf(obj);
        }
        writer.writeNext(entries);
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
        ExportDialog exportDialog = new ExportDialog(demonstrator);
        exportDialog.setVisible(true);
    }
}
