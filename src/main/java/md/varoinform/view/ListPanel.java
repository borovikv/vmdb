package md.varoinform.view;

import md.varoinform.controller.EnterpriseView;
import md.varoinform.model.entities.Enterprise;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/30/13
 * Time: 10:33 AM
 */
public class ListPanel extends JPanel implements Demonstrator {
    private JList list;
    private JEditorPane editorPane;
    private static String templatesPath = File.separator + "templates" + File.separator;
    private List<Enterprise> currentEnterprises;


    public ListPanel() {
        setLayout(new BorderLayout());

        editorPane = createEditorPane();
        list = createList(editorPane);
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(list), new JScrollPane(editorPane));
        add(splitPane);
    }


    private JEditorPane createEditorPane() {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
        editorPane.setEditable(false);
        editorPane.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    browse(e.getURL());
                }
            }
        });
        return editorPane;
    }


    private void browse(URL url) {
        if(!Desktop.isDesktopSupported() || url == null) return;
        URI uri = getUri(url);
        try {
            Desktop.getDesktop().browse(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private URI getUri(URL url){
        URI uri = null;
        try {
            uri = url.toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return uri;
    }



    private JList createList(JEditorPane editorPane) {
        JList list = new JList();
        //noinspection unchecked
        list.setCellRenderer(new EnterpriseCellRender());
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.addListSelectionListener(new SelectionListener(editorPane));
        return list;
    }

    @Override
    public void showResults(List<Enterprise> enterprises){
        currentEnterprises = enterprises;
        DefaultListModel model = new DefaultListModel();
        for (Enterprise enterprise : enterprises) {
            //noinspection unchecked
            model.addElement(enterprise);
        }
        //noinspection unchecked
        list.setModel(model);

    }


    @Override
    public List<Enterprise> getSelected(){
        //noinspection unchecked
        return list.getSelectedValuesList();
    }

    @Override
    public List<Enterprise> getALL() {
        return currentEnterprises;
    }

    @Override
    public void clear() {
        DefaultListModel model = (DefaultListModel) list.getModel();
        model.clear();
    }

    private static class SelectionListener implements ListSelectionListener {

        private final JEditorPane editorPane;

        public SelectionListener(JEditorPane editorPane) {
            this.editorPane = editorPane;
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                JList source = (JList) e.getSource();
                Enterprise enterprise = (Enterprise)source.getSelectedValue();
                if ( enterprise != null ) {
                    editorPane.setText(EnterpriseView.getView(enterprise));
                } else {
                    editorPane.setText("");
                }
            }
        }
    }
}
