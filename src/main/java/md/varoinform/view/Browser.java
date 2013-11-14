package md.varoinform.view;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/14/13
 * Time: 10:18 AM
 */
public class Browser extends JEditorPane {
    public Browser() {
        setContentType("text/html");
        HTMLEditorKit kit = new HTMLEditorKit();
        StyleSheet s = getStyleSheet();
        kit.setStyleSheet(s);
        setEditorKit(kit);
        setEditable(false);
        addHyperlinkListener(new BrowserHyperlinkListener());
    }


    private StyleSheet getStyleSheet() {
        StyleSheet s = new StyleSheet();
        URL styleUrl = null;
        try {
            styleUrl = new URL("file:/home/drifter/development/idea/VaroDB/src/main/resources/style.css");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        s.importStyleSheet(styleUrl);
        return s;
    }

    private static class BrowserHyperlinkListener implements HyperlinkListener {
        public void hyperlinkUpdate(HyperlinkEvent e) {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                browse(e.getURL());
            }
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
    }

}
