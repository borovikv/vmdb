package md.varoinform.creator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 3/20/14
 * Time: 11:29 AM
 */
public class Sender {
    private static final String WWW = "http://localhost:8000/manage/upload/";

    public void send(File file)  {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(WWW);
        System.out.println(1);
        MultipartEntityBuilder meb = MultipartEntityBuilder.create();
        meb.addBinaryBody("db", file);
        meb.addTextBody("title", file.getName());
        HttpEntity entity = meb.build();

        httpPost.setEntity(entity);

        try {
            HttpResponse response = client.execute(httpPost);
            printRequest(response.getEntity().getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printRequest(InputStream content) throws IOException {
        int c;
        StringBuilder builder = new StringBuilder();
        while ((c =content.read())!= -1) {
            builder.append((char)c);
        }
        System.out.println(builder.toString());
    }

    public static void main(String[] args) {
        new Sender().send(new File("/home/drifter/DB/csvdb_1/DB.h2.db"));
    }
}
