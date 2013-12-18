package md.varoinform.sequrity;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/14/13
 * Time: 12:29 PM
 */
public class RegistrationException extends Throwable {
    public static final String CONNECTION_ERROR = "CONNECTION ERROR";
    public static final String RESPONSE_ERROR = "SERVER ERROR";

    public RegistrationException(String message) {
        super(message);
    }

    public RegistrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegistrationException(Throwable cause) {
        super(cause);
    }
}
