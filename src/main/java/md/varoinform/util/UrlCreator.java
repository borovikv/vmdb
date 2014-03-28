package md.varoinform.util;

import md.varoinform.Settings;

import java.net.MalformedURLException;
import java.net.URL;

/**
* Created with IntelliJ IDEA.
* User: Vladimir Borovic
* Date: 3/28/14
* Time: 3:45 PM
*/
public class UrlCreator {
    private String check;
    private String confirm;
    private String url;

    public UrlCreator(String uid) {
        this.check = "";
        this.confirm = "";
        url = Settings.getUpdateUrl() + "?user=" + uid;
    }

    public void setCheck(boolean check){
        if (check)
            this.check = "&check=true";
        else{
            this.check = "";
        }
    }

    public void setConfirm(boolean confirm){
        if (confirm){
            this.confirm = "&confirm=true";
        } else {
            this.confirm = "";
        }
    }

    public String getString(){
        return url + check + confirm;
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
