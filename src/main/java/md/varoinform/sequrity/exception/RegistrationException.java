package md.varoinform.sequrity.exception;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/14/13
 * Time: 12:29 PM
 */
public class RegistrationException extends Throwable {

    private Error error;

    public RegistrationException(Error error) {
        super(error.getText());
        this.error = error;
    }

    public RegistrationException(Error error, IOException e) {
        super(error.getText(), e);
        this.error = error;
    }

    public Error getError() {
        return error;
    }
}
