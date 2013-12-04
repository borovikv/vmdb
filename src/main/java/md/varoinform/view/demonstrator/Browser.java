package md.varoinform.view.demonstrator;

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

        StyleSheet styleSheet = getStyleSheet();
        if (styleSheet != null){
            HTMLEditorKit kit = new HTMLEditorKit();
            kit.setStyleSheet(styleSheet);
            setEditorKit(kit);
        }

        setEditable(false);

        addHyperlinkListener(new BrowserHyperlinkListener());
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
            //File f = new File("style.css");
            //System.out.println(f.toURI());
            styleUrl = new URL("file:/home/drifter/development/idea/VaroDB/src/main/resources/style.css");
            //System.out.println(styleUrl);
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

}
