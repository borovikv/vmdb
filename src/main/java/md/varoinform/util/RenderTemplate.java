package md.varoinform.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/1/13
 * Time: 11:44 AM
 */
public class RenderTemplate {
    private String template;
    public RenderTemplate(String fileName) {
        URL url = getClass().getResource(fileName);
        System.out.println(url + fileName);
        template = downloadPage(url);
    }

    // Download page at given URL.
    private String downloadPage(URL pageUrl) {
        try {
            // Open connection to URL for reading.
            BufferedReader reader = new BufferedReader(new InputStreamReader(pageUrl.openStream()));

            // Read page into buffer.
            String line;
            StringBuffer pageBuffer = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                pageBuffer.append(line);
            }

            return pageBuffer.toString();
        } catch (Exception e) {e.printStackTrace();}

        return null;
    }//
    public String renderTemplate(String s){//Map<String, String> context){
        return "<html><body>" + template + "<br> " + s + "</body></html>";

    }
}
