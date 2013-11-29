package md.varoinform.view.dialogs;

import au.com.bytecode.opencsv.CSVWriter;
import md.varoinform.controller.comparators.ColumnPriorityComparator;
import md.varoinform.controller.entityproxy.EnterpriseProxy;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.util.ResourceBundleHelper;
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
    private RowsChoosePanel rowsChoosePanel = new RowsChoosePanel("export rows");

    public ExportDialog(Component parent, Demonstrator demonstrator) {
        this.demonstrator = demonstrator;

        setSize(500, 500);
        setModal(true);
        setLocationRelativeTo(parent);

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
            CSVWriter writer = new CSVWriter(new FileWriter(selectedFile), ',');
            List<String> selectedColumns = fieldChooser.getSelectedFieldNames();
            Collections.sort(selectedColumns, new ColumnPriorityComparator());
            String[] entries = new String[selectedColumns.size()];

            for (Enterprise enterprise : getEnterprises()) {
                EnterpriseProxy proxy = new EnterpriseProxy(enterprise);
                for (int i = 0; i < selectedColumns.size(); i++) {
                     entries[i] = proxy.get(selectedColumns.get(i));
                }
                writer.writeNext(entries);
            }
            writer.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private List<Enterprise> getEnterprises() {
        if (rowsChoosePanel.getChoose() == RowsChoosePanel.ALL){
            return demonstrator.getALL();
        }
        return demonstrator.getSelected();
    }

}
