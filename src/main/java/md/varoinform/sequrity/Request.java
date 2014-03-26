package md.varoinform.sequrity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Request {

    private String url;

    public Request(String url) {
        this.url = url;
    }

    String request() throws IOException {
        String response;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(this.url);
            connection = getHttpURLConnection(url);
            response = getResponse(connection);

        } finally {
            if (connection != null) connection.disconnect();
        }
        return response;
    }

    HttpURLConnection getHttpURLConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        return connection;
    }

    String getResponse(HttpURLConnection connection) throws IOException {
        String response;

        try (InputStream inputStream = connection.getInputStream()) {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            copy(inputStream, outputStream);
            response = outputStream.toString();

        } catch (IOException e) {

            throw new IOException(e);
        }

        return response;
    }

    void copy(InputStream inputStream, ByteArrayOutputStream outputStream) throws IOException {
        int c;
        while ((c = inputStream.read()) != -1) {
            outputStream.write(c);
        }
    }
}