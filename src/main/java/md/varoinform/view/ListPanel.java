package md.varoinform.view;

import md.varoinform.model.dao.GenericDaoHibernateImpl;
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
public class ListPanel extends JPanel{
    private JList list;
    private JEditorPane editorPane;
    private static String templatesPath = File.separator + "templates" + File.separator;

    public ListPanel() {
        setLayout(new BorderLayout());

        editorPane = createEditorPane();
        list = createList(editorPane);
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(list), new JScrollPane(editorPane));
        add(splitPane);

        showAll();
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
        RenderTemplate cellRenderer = new RenderTemplate(templatesPath  + "cellTemplate.html");
        list.setCellRenderer(new EnterpriseCellRender(cellRenderer));
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.addListSelectionListener(new SelectionListener(editorPane));
        return list;
    }


    private void showAll(){
        GenericDaoHibernateImpl<Enterprise, Long> eDao = new GenericDaoHibernateImpl<Enterprise, Long>(Enterprise.class);
        java.util.List<Enterprise> enterprises = eDao.getAll();
        showResults(enterprises);
        System.out.println("******************************************************************************************");
        System.out.println(enterprises.size());
        System.out.println("******************************************************************************************");
    }


    public void showResults(List<Enterprise> enterprises){
        DefaultListModel model = new DefaultListModel();
        for (Enterprise enterprise : enterprises) {
            model.addElement(enterprise);
        }
        list.setModel(model);
    }


    public List getSelected(){
        return list.getSelectedValuesList();
    }


    private static class SelectionListener implements ListSelectionListener {

        private final JEditorPane editorPane;
        private RenderTemplate extendedViewRenderer;

        public SelectionListener(JEditorPane editorPane) {
            this.editorPane = editorPane;
            extendedViewRenderer = new RenderTemplate(templatesPath + "extendedTemplate.html");
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting() == false) {
                JList source = (JList) e.getSource();
                Enterprise enterprise = (Enterprise)source.getSelectedValue();
                editorPane.setText(EnterpriseView.getView(enterprise, extendedViewRenderer));
            }
        }
    }
}
