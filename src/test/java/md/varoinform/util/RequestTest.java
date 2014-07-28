package md.varoinform.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class RequestTest {

    @Test
    public void testGetViaProxy() throws Exception {
        String hostname = "192.168.0.254";
        Integer port = 8080;
        final String user = "borovik";
        final String password = "Backspace!";
        String s = new Request("http://yp.md/12132").getViaProxy(1, hostname, port, user, password);
        System.out.println(s);
        assertNotNull(s);
    }
}