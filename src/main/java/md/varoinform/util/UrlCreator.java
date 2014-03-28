package md.varoinform.util;

import md.varoinform.Settings;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
* Created with IntelliJ IDEA.
* User: Vladimir Borovic
* Date: 3/28/14
* Time: 3:45 PM
*/
public class UrlCreator {
    private String url;
    private Map<String, String> params = new HashMap<>();

    public UrlCreator(String uid) {
        url = Settings.getUpdateUrl() + "?user=" + uid;
    }

    public void setParam(String name, String value) {
        params.put(name, value);
    }

    public String getString(){
        String delimiter = "&";
        for (String name : params.keySet()) {
            String value = params.get(name);
            url += delimiter + name + "=" + value;
        }
        return url;
    }

    public URL getUrl(){
        try {
            return new URL(getString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
