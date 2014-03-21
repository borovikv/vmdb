package md.varoinform.creator;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 3/19/14
 * Time: 3:01 PM
 */
public class SenderException extends Exception {
    public SenderException(String message) {
        super(message);
    }

    public SenderException(Throwable cause) {
        super(cause);
    }
}
