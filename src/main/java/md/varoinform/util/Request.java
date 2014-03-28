package md.varoinform.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Request {

    private String url;

    public Request(String url) {
        this.url = url;
    }

    public String timesGet(int times) throws IOException {
        if (times <= 0) return null;
        try {
            Response execute = org.apache.http.client.fluent.Request.Get(url).execute();
            HttpResponse response = execute.returnResponse();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            return out.toString();
        } catch (IOException e) {
            if (times == 1) throw e;
            return timesGet(times - 1);
        }
    }
}