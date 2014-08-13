package md.varoinform.util;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;

//ToDo: close connections after request
public class Request {

    private String url;
    private final PreferencesHelper helper;

    public Request(String url) {
        this.url = url;
        helper = new PreferencesHelper();
    }

    public String timesGet(int times) throws IOException {
        if (times <= 0) return null;

        if (!helper.getUseProxy()){
            return getSimple(times);
        } else {
            String hostname = helper.getProxyAddress();
            Integer port = helper.getProxyPort();
            final String user = helper.getProxyUser();
            final String password = helper.getProxyPassword();
            return getViaProxy(times, hostname, port, user, password);
        }
    }

    public String getViaProxy(int times, String hostname, int port, final String user, final String password) throws IOException {
        if (times <= 0) return null;

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(hostname, port));
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password.toCharArray());
            }
        };
        Authenticator.setDefault(authenticator);
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection(proxy);
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            return IOUtils.toString(inputStream);
        } catch (IOException e) {
            if (times == 1) throw e;
            return getViaProxy(times - 1, hostname, port, user, password);
        }
    }

    public String getSimple(int times) throws IOException {
        if (times <= 0) return null;

        try {
            Response execute = org.apache.http.client.fluent.Request.Get(url).execute();
            HttpResponse response = execute.returnResponse();
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode == 404) return "404";
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            return out.toString();
        } catch (IOException e) {
            if (times == 1) throw e;
            return getSimple(times - 1);
        }
    }

}