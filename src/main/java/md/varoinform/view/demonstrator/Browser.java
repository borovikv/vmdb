package md.varoinform.view.demonstrator;

import md.varoinform.Settings;
import md.varoinform.util.ResourceBundleHelper;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/14/13
 * Time: 10:18 AM
 */
public class Browser extends JEditorPane {
    public Browser() {
        setContentType("text/html");

        StyleSheet styleSheet = getStyleSheet();
        if (styleSheet != null){
            HTMLEditorKit kit = new HTMLEditorKit();
            kit.setStyleSheet(styleSheet);
            setEditorKit(kit);
        }

        setEditable(false);
        setComponentPopupMenu(createPopupMenu());
        addHyperlinkListener(new BrowserHyperlinkListener());
    }

    private JPopupMenu createPopupMenu() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem copy = new JMenuItem(ResourceBundleHelper.getString("copy", "Copy"));
        copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copy();
            }
        });
        menu.add(copy);
        return menu;
    }

    private StyleSheet getStyleSheet() {
        URL styleUrl = getStyleSheetUrl();
        if (styleUrl == null) return null;

        StyleSheet s = new StyleSheet();
        s.importStyleSheet(styleUrl);
        return s;
    }

    private URL getStyleSheetUrl() {
        URL styleUrl = null;
        try {
            styleUrl = Paths.get(Settings.getWorkFolder(), "external-resources", "style.css").toUri().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return styleUrl;
    }

    private static class BrowserHyperlinkListener implements HyperlinkListener {

        public void hyperlinkUpdate(HyperlinkEvent e) {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                browse(e.getURL());
            }
        }

        private void browse(URL url) {
            if(!Desktop.isDesktopSupported() || url == null) return;

            try {
                URI uri = url.toURI();
                Desktop.getDesktop().browse(uri);
            } catch (IOException|URISyntaxException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void setText(String t) {
        super.setText(t);
        setCaretPosition(0);
    }
}
